package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.TaskRepository;
import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.CreateTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.dto.user.SetExecutorTaskDto;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.enums.TaskStatus;
import com.example.taskmanagementsystem.security.jwt.JwtUser;
import com.example.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;

import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.InsufficientPermissionsException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUser jwtUser;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testCreateOrUpdateWhenCreatorIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setCreatorId(100L);

        when(userService.findById(100L)).thenReturn(null);

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.createOrUpdate(taskDto));
        assertNotNull(throwable);
        assertEquals("User with id " + taskDto.getCreatorId() + " not found", throwable.getMessage());
    }

    @Test
    public void testCreateOrUpdateWhenCreatorIsNotNullAndHasPermission() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setCreatorId(1L);
        taskDto.setExecutorId(2L);
        when(jwtUser.getId()).thenReturn(1L);

        User creator = User.builder().build();
        creator.setId(1L);

        User executor = User.builder().build();
        executor.setId(2L);

        when(userService.findById(1L)).thenReturn(creator);
        when(userService.findById(2L)).thenReturn(executor);

        when(taskRepository.save(any(Task.class))).thenReturn(taskDto.toTask());

        TaskDto result = taskService.createOrUpdate(taskDto);

        assertNotNull(result);
    }

    @Test
    public void testCreateOrUpdateWhenCreatorIdNotEqualCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long currentUserId = 1L;
        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setId(1L);
        taskDto.setCreatorId(100L);

        when(jwtUser.getId()).thenReturn(currentUserId);

        User creator = User.builder().build();
        creator.setId(100L);

        when(userService.findById(100L)).thenReturn(creator);
        //Когда пытаемся изменить задачу(с id != null)
        Throwable throwable = assertThrows(InsufficientPermissionsException.class, () -> taskService.createOrUpdate(taskDto));
        assertNotNull(throwable);
        assertEquals("The user is trying to change someone else's task.", throwable.getMessage());

        //Когда пытаемся добавить задачу(с id == null)
        taskDto.setId(null);
        Throwable throwable1 = assertThrows(InsufficientPermissionsException.class, () -> taskService.createOrUpdate(taskDto));
        assertNotNull(throwable1);
        assertEquals("The user is trying to create a task not on his own behalf.", throwable1.getMessage());
    }

    @Test
    public void testCreateOrUpdateWhenExecutorIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setCreatorId(1L);
        taskDto.setExecutorId(200L);

        User creator = User.builder().build();
        creator.setId(1L);

        when(jwtUser.getId()).thenReturn(1L);

        when(userService.findById(1L)).thenReturn(creator);
        when(userService.findById(200L)).thenReturn(null);

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.createOrUpdate(taskDto));
        assertNotNull(throwable);
        assertEquals("User with id " + taskDto.getExecutorId() + " not found", throwable.getMessage());
    }

    @Test
    public void testCreateOrUpdateWhenAllDataIsProvided() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        CreateTaskDto taskDto = new CreateTaskDto();
        taskDto.setTitle("test");
        taskDto.setCreatorId(1L);
        taskDto.setExecutorId(2L);

        User creator = User.builder().build();
        creator.setId(1L);

        User executor = User.builder().build();
        executor.setId(2L);

        when(jwtUser.getId()).thenReturn(1L);

        when(userService.findById(1L)).thenReturn(creator);
        when(userService.findById(2L)).thenReturn(executor);

        when(taskRepository.save(any(Task.class))).thenReturn(taskDto.toTask());

        TaskDto result = taskService.createOrUpdate(taskDto);
        assertNotNull(result);
        assertEquals(taskDto.getTitle(), result.getTitle());
    }

    @Test
    public void testFindAllWhenTaskFilterDtoIsNullAndUserHasUserRole() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        when(jwtUser.getId()).thenReturn(1L);
        User currentUser = User.builder().build();
        currentUser.setRoles(Collections.singletonList(new Role("ROLE_USER")));

        when(userService.findById(1L)).thenReturn(currentUser);

        // Создание тестовых задач
        List<Task> tasks = Arrays.asList(new Task(), new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> result = taskService.findAll(null);

        assertEquals(3, result.size());
    }

    @Test
    public void testFindAllWhenTaskFilterDtoIsNullAndUserDoesNotHaveUserRole() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        when(jwtUser.getId()).thenReturn(1L);
        User currentUser = User.builder().build();
        // Устанавливаем пользователя без роли USER
        currentUser.setRoles(Collections.singletonList(new Role("ROLE_EXECUTOR")));

        when(userService.findById(1L)).thenReturn(currentUser);

        List<Task> tasks = Collections.singletonList(new Task());
        when(taskRepository.findAll(any(Specification.class))).thenReturn(tasks);

        List<TaskDto> result = taskService.findAll(null);

        assertEquals(1, result.size());
    }

    @Test
    public void testFindAllWhenTaskFilterDtoIsNotNullAndPageableIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);


        TaskFilterDto taskFilterDto = new TaskFilterDto();

        when(jwtUser.getId()).thenReturn(1L);
        User currentUser = User.builder().build();
        // Устанавливаем пользователя без роли USER
        currentUser.setRoles(Collections.singletonList(new Role("ROLE_EXECUTOR")));

        when(userService.findById(1L)).thenReturn(currentUser);

        List<Task> tasks = Collections.singletonList(new Task());
        when(taskRepository.findAll(any(Specification.class))).thenReturn(tasks);

        List<TaskDto> result = taskService.findAll(taskFilterDto);

        assertEquals(1, result.size());

        taskFilterDto.setSize(1);
        List<TaskDto> result2 = taskService.findAll(taskFilterDto);

        assertEquals(1, result2.size());
        taskFilterDto.setSize(null);
        taskFilterDto.setPage(1);
        List<TaskDto> result3 = taskService.findAll(taskFilterDto);

        assertEquals(1, result3.size());
    }

    @Test
    public void testFindAllWhenTaskFilterDtoIsNotNullAndPageableIsNotNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        TaskFilterDto taskFilterDto = new TaskFilterDto();
        taskFilterDto.setSize(10);
        taskFilterDto.setPage(0);

        when(jwtUser.getId()).thenReturn(1L);
        User currentUser = User.builder().build();
        currentUser.setRoles(Collections.singletonList(new Role("ROLE_USER")));

        when(userService.findById(1L)).thenReturn(currentUser);

        Page<Task> page = new PageImpl<>(Collections.singletonList(new Task()));

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        List<TaskDto> result = taskService.findAll(taskFilterDto);

        assertEquals(1, result.size());
    }

    @Test
    public void testFindByIdWhenOptionalTaskIsEmpty() {
        SecurityContextHolder.setContext(securityContext);

        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.findById(taskId));
        assertNotNull(throwable);
        assertEquals("Task with id " + taskId + " not found", throwable.getMessage());
    }

    @Test
    public void testFindByIdWhenOptionalTaskIsPresent() {
        SecurityContextHolder.setContext(securityContext);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Sample Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDto result = taskService.findById(1L);

        assertNotNull(result);
        assertEquals(task.getTitle(), result.getTitle());
    }

    @Test
    public void testDeleteByIdWhenTaskIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.deleteById(taskId));
        assertNotNull(throwable);
        assertEquals("Task with id " + taskId + " not found", throwable.getMessage());
    }

    @Test
    public void testDeleteByIdWhenTaskCreatorIdNotEqualCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        Long creatorId = 1L;
        Long currentUserId = 2L;

        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(currentUserId);

        Throwable throwable = assertThrows(InsufficientPermissionsException.class, () -> taskService.deleteById(taskId));
        assertNotNull(throwable);
        assertEquals("The user is trying to delete someone else's task.", throwable.getMessage());
    }

    @Test
    public void testDeleteByIdWhenTaskCreatorIdEqualsCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        Long creatorId = 1L;

        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);

        Optional<Task> optionalTask = Optional.of(task);
        when(taskRepository.findById(taskId)).thenReturn(optionalTask);
        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(creatorId);

        taskService.deleteById(taskId);
        verify(taskRepository).delete(optionalTask.get());
    }

    @Test
    public void testSetExecutorWhenOptionalTaskIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        SetExecutorTaskDto taskDto = new SetExecutorTaskDto(1L, 2L);
        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.setExecutor(taskDto));
        assertNotNull(throwable);
        assertEquals("Task with id " + taskDto.getId() + " not found", throwable.getMessage());
    }

    @Test
    public void testSetExecutorWhenOptionalTaskCreatorIdNotEqualCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        long creatorId = 1L;
        long currentUserId = 2L;

        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(currentUserId);


        SetExecutorTaskDto taskDto = new SetExecutorTaskDto(taskId, 3L);
        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Throwable throwable = assertThrows(InsufficientPermissionsException.class, () -> taskService.setExecutor(taskDto));
        assertNotNull(throwable);
        assertEquals("The user is trying to assign someone else's task.", throwable.getMessage());
    }

    @Test
    public void testSetExecutorWhenOptionalTaskCreatorIdEqualsCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        long creatorId = 1L;
        long currentUserId = 1L;

        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(currentUserId);

        SetExecutorTaskDto taskDto = new SetExecutorTaskDto(taskId, 3L);
        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);

        User executor = User.builder().build();
        executor.setId(creatorId);
        when(userService.findById(3L)).thenReturn(executor);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDto expectedTaskDto = new TaskDto();
        expectedTaskDto.setTitle("test");

        when(taskRepository.save(any(Task.class))).thenReturn(expectedTaskDto.toTask());

        TaskDto result = taskService.setExecutor(taskDto);

        assertNotNull(result);
        assertEquals(expectedTaskDto.getTitle(), result.getTitle());
    }

    @Test
    public void testChangeStatusWhenOptionalTaskIsNull() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        ChangeTaskStatusDto taskStatusDto = new ChangeTaskStatusDto(1L, TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskStatusDto.getTaskId())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> taskService.changeStatus(taskStatusDto));
        assertNotNull(throwable);
        assertEquals("Task with id " + taskStatusDto.getTaskId() + " not found", throwable.getMessage());
    }

    @Test
    public void testChangeStatusWhenOptionalTaskCreatorIdNotEqualCurrentUserId() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        long creatorId = 1L;
        long currentUserId = 100L;

        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(currentUserId);

        ChangeTaskStatusDto taskStatusDto = new ChangeTaskStatusDto(taskId, TaskStatus.IN_PROGRESS);
        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        User executor = User.builder().build();
        executor.setId(2L);
        task.setExecutor(executor);
        task.setCreator(creator);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Throwable throwable = assertThrows(InsufficientPermissionsException.class, () -> taskService.changeStatus(taskStatusDto));
        assertNotNull(throwable);
        assertEquals("The user is trying to assign a new status someone else's task.", throwable.getMessage());
    }

    @Test
    public void testChangeStatusWhenOptionalTaskCreatorIdEqualsCurrentUserIdAndStatusMatches() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        long creatorId = 1L;
        TaskStatus status = TaskStatus.IN_PROGRESS;

        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(creatorId);

        ChangeTaskStatusDto taskStatusDto = new ChangeTaskStatusDto(taskId, status);
        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);
        User executor = User.builder().build();
        executor.setId(2L);
        task.setExecutor(executor);
        task.setStatus(status);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TaskDto result = taskService.changeStatus(taskStatusDto);

        assertNotNull(result);
        assertEquals(status, result.getStatus());
    }

    @Test
    public void testChangeStatusWhenOptionalTaskCreatorIdEqualsCurrentUserIdAndStatusDoesNotMatch() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwtUser);

        Long taskId = 1L;
        long creatorId = 1L;
        TaskStatus oldStatus = TaskStatus.IN_PROGRESS;
        TaskStatus newStatus = TaskStatus.COMPLETED;

        when(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).thenReturn(creatorId);

        ChangeTaskStatusDto taskStatusDto = new ChangeTaskStatusDto(taskId, newStatus);
        Task task = new Task();
        task.setId(taskId);
        User creator = User.builder().build();
        creator.setId(creatorId);
        task.setCreator(creator);
        task.setStatus(oldStatus);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).then(returnsFirstArg());

        TaskDto result = taskService.changeStatus(taskStatusDto);

        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
    }
}