package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.TaskRepository;
import com.example.taskmanagementsystem.dto.task.ChangeTaskStatusDto;
import com.example.taskmanagementsystem.dto.task.CreateTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskFilterDto;
import com.example.taskmanagementsystem.dto.user.SetExecutorTaskDto;
import com.example.taskmanagementsystem.dto.task.TaskDto;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.entity.utils.TaskSpecification;
import com.example.taskmanagementsystem.exception.InsufficientPermissionsException;
import com.example.taskmanagementsystem.security.jwt.JwtUser;
import com.example.taskmanagementsystem.service.TaskService;
import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public TaskDto createOrUpdate(CreateTaskDto taskDto) {
        //получаем id текущего пользователя
        Long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User creator = userService.findById(taskDto.getCreatorId());
        if (creator == null) {
            log.warn("In createOrUpdate user with id {} not found", taskDto.getCreatorId());
            throw new EntityNotFoundException("User with id " + taskDto.getCreatorId() + " not found");
        }
        if (!creator.getId().equals(currentUserId)) {
            if (taskDto.getId() != null) {
                log.warn("In createOrUpdate, a user with identifier {} tried to change a task {} that was not his", currentUserId, taskDto);
                throw new InsufficientPermissionsException("The user is trying to change someone else's task.");
            } else {
                log.warn("In createOrUpdate, a user with identifier {} tried to create a task {} not on his behalf", currentUserId, taskDto);
                throw new InsufficientPermissionsException("The user is trying to create a task not on his own behalf.");
            }
        }
        User executor = userService.findById(taskDto.getExecutorId());
        if (executor == null) {
            log.warn("In createOrUpdate user with id {} not found", taskDto.getExecutorId());
            throw new EntityNotFoundException("User with id " + taskDto.getExecutorId() + " not found");
        }
        Task task = taskDto.toTask();
        task.setCreator(creator);
        task.setExecutor(executor);
        Task savedTask = taskRepository.save(task);
        log.info("In createOrUpdate task {} successfully saved", savedTask);
        return TaskDto.fromTask(savedTask);
    }

    @Override
    @Transactional
    public List<TaskDto> findAll(@Nullable TaskFilterDto taskFilterDto) {
        Long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User currentUser = userService.findById(currentUserId);
        Role roleUser = currentUser.getRoles()
                .stream().filter(r -> r.getName().equals("ROLE_USER"))
                .findFirst().orElse(null);
        if (taskFilterDto == null) {            // Если нет фильтров и пользователь не имеет роли Юзер, то насильно задаем фильтр где он executor
            List<Task> tasks;
            if (roleUser == null) {
                tasks = taskRepository.findAll(TaskSpecification.taskFilter(TaskFilterDto.builder().executorId(currentUserId).build()));
            } else {
                tasks = taskRepository.findAll();
            }
            log.info("In findAll found {} task(s) without filtration and pagination", tasks.size());
            return tasks.stream().map(TaskDto::fromTask).toList();
        }
        if (roleUser == null) {
            //Если пользователь не имеет роли USER, то насильно в критерию добавляем поиск по исполнителю его id
            taskFilterDto.setExecutorId(currentUserId);
        }
        Pageable pageable = createPageable(taskFilterDto);
        Specification<Task> specification = TaskSpecification.taskFilter(taskFilterDto);
        if (pageable == null) {
            List<Task> tasks = taskRepository.findAll(specification);
            log.info("In findAll found {} task(s) without pagination", tasks.size());
            return tasks.stream().map(TaskDto::fromTask).toList();
        }
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        log.info("In findAll found {} task(s) with filtration and pagination", tasks.getSize());
        return tasks.stream().map(TaskDto::fromTask).toList();
    }

    @Override
    @Transactional
    public TaskDto findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            log.warn("In findById task with id {} not found", id);
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        log.info("In findById task {} found by id {}", optionalTask.get(), id);
        return TaskDto.fromTask(optionalTask.get());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty()) {
            log.warn("In deleteById task with id {} not found", id);
            throw new EntityNotFoundException("Task with id " + id + " not found");
        }
        if (!task.get().getCreator().getId().equals(currentUserId)) {
            log.warn("In deleteById, a user with the identifier {} tried to delete a task that was not his own {}.", currentUserId, task.get());
            throw new InsufficientPermissionsException("The user is trying to delete someone else's task.");
        }
        taskRepository.delete(task.get());
        log.info("In deleteById task with id {} found and successfully deleted", id);
    }

    @Override
    @Transactional
    public TaskDto setExecutor(SetExecutorTaskDto taskDto) {
        Long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
        if (optionalTask.isEmpty()) {
            log.warn("In setExecutor task with id {} not found", taskDto.getId());
            throw new EntityNotFoundException("Task with id " + taskDto.getId() + " not found");
        }
        if (!optionalTask.get().getCreator().getId().equals(currentUserId)) {
            log.warn("In setExecutor, a user with id {} tried to assign someone else's task to an executor {}.", currentUserId, optionalTask.get());
            throw new InsufficientPermissionsException("The user is trying to assign someone else's task.");
        }
        Task task = optionalTask.get();
        User executor = userService.findById(taskDto.getExecutorId());
        task.setExecutor(executor);
        log.info("In setExecutor the executor {} has successfully assigned the task {}", executor, task);
        return TaskDto.fromTask(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto changeStatus(ChangeTaskStatusDto taskStatusDto) {
        Long currentUserId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Optional<Task> optionalTask = taskRepository.findById(taskStatusDto.getTaskId());
        if (optionalTask.isEmpty()) {
            log.warn("In changeStatus task with id {} not found", taskStatusDto.getTaskId());
            throw new EntityNotFoundException("Task with id " + taskStatusDto.getTaskId() + " not found");
        }
        if (!optionalTask.get().getCreator().getId().equals(currentUserId)
                && !optionalTask.get().getExecutor().getId().equals(currentUserId)) {
            log.warn("In changeStatus user with id {} tried to assign a new status to a task for which he is not the creator or executor {}", currentUserId, optionalTask.get());
            throw new InsufficientPermissionsException("The user is trying to assign a new status someone else's task.");
        }
        Task task = optionalTask.get();
        if (task.getStatus() == taskStatusDto.getNewStatus()) {
            log.warn("In changeStatus task {} already has the status {}", task, task.getStatus());
            return TaskDto.fromTask(task);
        }
        task.setStatus(taskStatusDto.getNewStatus());
        log.info("In changeStatus task {} was successfully assigned a new status {}", task, taskStatusDto.getNewStatus());
        return TaskDto.fromTask(taskRepository.save(task));
    }

    private Pageable createPageable(TaskFilterDto taskFilterDto) {
        Pageable pageable = null;
        if (taskFilterDto.getPage() != null && taskFilterDto.getSize() != null) {
            log.info("In createPageable found limit {} for page {}", taskFilterDto.getSize(), taskFilterDto.getPage());
            pageable = PageRequest.of(taskFilterDto.getPage(), taskFilterDto.getSize());
        }
        return pageable;
    }
}