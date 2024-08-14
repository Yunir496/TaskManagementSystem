package com.example.taskmanagementsystem.service.impl;

import com.example.taskmanagementsystem.dto.auth.AuthenticationRequestDto;
import com.example.taskmanagementsystem.dto.user.CreateUserDto;
import com.example.taskmanagementsystem.dto.user.UserDto;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.UserNotRegisteredException;
import com.example.taskmanagementsystem.security.jwt.JwtTokenProvider;
import com.example.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    public void testAuthenticateUserWhenUserFound() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto("test@example.com", "password");
        User user = User.builder().email("test@example.com").build();

        when(userService.findByEmail(requestDto.getEmail())).thenReturn(user);
        when(jwtTokenProvider.createToken(requestDto.getEmail(), user.getRoles())).thenReturn("dummyToken");

        Map<Object, Object> response = authenticationService.authenticateUser(requestDto);

        assertNotNull(response);
        assertEquals("test@example.com", response.get("email"));
        assertEquals("dummyToken", response.get("token"));
    }

    @Test
    public void testAuthenticateUserWhenUserNotFound() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto("test@example.com", "password");

        when(userService.findByEmail(requestDto.getEmail())).thenReturn(null);

        Throwable throwable = assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUser(requestDto));
        assertNotNull(throwable);
        assertEquals("Invalid email or password", throwable.getMessage());
    }

    @Test
    public void testAuthenticateUserWhenAuthenticationExceptionOccurs() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto("test@example.com", "password");

        doThrow(new AccountExpiredException("account expired")).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        Throwable throwable = assertThrows(BadCredentialsException.class, () -> authenticationService.authenticateUser(requestDto));
        assertNotNull(throwable);
        assertEquals("Invalid email or password", throwable.getMessage());
    }

    @Test
    public void testCreateUserWhenRegisterIsNull() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test@example.com")
                .password("password")
                .isUser(true).build();

        when(userService.register(any(User.class), eq(true))).thenReturn(null);

        Throwable throwable = assertThrows(UserNotRegisteredException.class, () -> authenticationService.createUser(createUserDto));
        assertNotNull(throwable);
        assertEquals("User " + createUserDto + " not registered", throwable.getMessage());
    }

    @Test
    public void testCreateUserWhenAllDataExists() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .email("test@example.com")
                .password("password")
                .isUser(true).build();
        UserDto userDto = new UserDto();

        when(userService.register(any(User.class), eq(true))).thenReturn(userDto);

        UserDto result = authenticationService.createUser(createUserDto);
        assertNotNull(result);
        assertEquals(userDto, result);
    }
}