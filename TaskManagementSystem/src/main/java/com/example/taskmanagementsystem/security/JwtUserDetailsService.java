package com.example.taskmanagementsystem.security;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.security.jwt.JwtUser;
import com.example.taskmanagementsystem.security.jwt.JwtUserFactory;
import com.example.taskmanagementsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {        //Хоть метод и называется юзернейм, но будем искать по email: ТЗ требование пункт 1.
        User user = userService.findByEmail(email);
        if (user == null) {
            log.warn("In loadUserByUsername user with email {} not found", email);
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("In loadUserByUsername user with email {} successfully loaded", email);
        return jwtUser;
    }
}