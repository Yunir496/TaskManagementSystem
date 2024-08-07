package com.example.taskmanagementsystem.config;

import com.example.taskmanagementsystem.security.jwt.JwtConfigurer;
import com.example.taskmanagementsystem.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final static String USER_ENDPOINT = "/api/v1/user/**";
    private final static String EXECUTOR_ENDPOINT = "/api/v1/executor/**";
    private final static String LOGIN_ENDPOINT = "/api/v1/auth/login";


    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.httpBasic().disable()
               .csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authorizeRequests()
               .antMatchers(LOGIN_ENDPOINT).permitAll()
               .antMatchers(USER_ENDPOINT).hasRole("USER")
               .antMatchers(EXECUTOR_ENDPOINT).hasRole("EXECUTOR")
               .anyRequest().authenticated()
               .and()
               .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
