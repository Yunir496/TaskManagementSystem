package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dao.RoleRepository;
import com.example.taskmanagementsystem.dao.UserRepository;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.enums.Status;
import com.example.taskmanagementsystem.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterWithUserRole() {
        User user = User.builder().password("password").build();

        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            userArg.setId(1L);
            userArg.setFirstName("test");
            return userArg;
        });

        UserDto userDto = userService.register(user, true);

        assertEquals(Status.ACTIVE, user.getStatus());
        assertNotNull(user.getCreated());
        assertNotNull(user.getUpdated());
        assertEquals("ROLE_USER", user.getRoles().get(0).getName());
        assertEquals("test", userDto.getFirstName());
    }

    @Test
    public void testRegisterWithExecutorRole() {
        User user = User.builder().password("password").build();

        Role role = new Role();
        role.setName("ROLE_EXECUTOR");

        when(roleRepository.findByName("ROLE_EXECUTOR")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            userArg.setId(2L);
            userArg.setLastName("test");
            return userArg;
        });

        UserDto userDto = userService.register(user, false);

        assertEquals(Status.ACTIVE, user.getStatus());
        assertNotNull(user.getCreated());
        assertNotNull(user.getUpdated());
        assertEquals("ROLE_EXECUTOR", user.getRoles().get(0).getName());
        assertEquals("test", userDto.getLastName());
    }

    @Test
    public void testGetAllWhenDataExists() {
        List<User> users = Arrays.asList(User.builder().build(), User.builder().build());
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.getAll();

        assertNotNull(userDtos);
        assertEquals(2, userDtos.size());
    }

    @Test
    public void testGetAllWhenNoData() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> userDtos = userService.getAll();

        assertNotNull(userDtos);
        assertEquals(0, userDtos.size());
    }

    @Test
    public void testFindByEmail() {
        String testEmail = "test@example.com";
        User user = User.builder().email(testEmail).build();

        when(userRepository.findByEmail(testEmail)).thenReturn(user);

        User result = userService.findByEmail(testEmail);

        assertNotNull(result);
        assertEquals(testEmail, result.getEmail());
    }

    @Test
    public void testFindByIdWhenResultIsNull() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> userService.findById(id));
        assertNotNull(throwable);
        assertEquals("User with id " + id + " not found", throwable.getMessage());
    }

    @Test
    public void testFindByIdWhenResultIsNotNull() {
        Long id = 2L;
        User mockUser = User.builder().build();
        mockUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        User result = userService.findById(id);

        assertNotNull(result);
        assertEquals(mockUser, result);
        assertEquals(mockUser.getId(), result.getId());
    }

    @Test
    void testDeleteByIdWithExistingUser() {
        // Тест сценария, когда юзер найден
        User expectedUser = User.builder().email("test").build();
        Optional<User> optionalUser = Optional.of(expectedUser);
        when(userRepository.findById(anyLong())).thenReturn(optionalUser);

        userService.delete(1L);
        verify(userRepository).delete(expectedUser);
    }

    @Test
    void testDeleteByIdWithNonExistingUser() {
        // Тест сценария, когда юзер не найден
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
        assertNotNull(throwable);
        assertEquals("User with id 1 not found", throwable.getMessage());
    }
}