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

/**
 * Конфигурация безопасности приложения.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final static String USER_ENDPOINT = "/api/v1/user/**";
    private final static String EXECUTOR_ENDPOINT = "/api/v1/executor/**";
    private final static String AUTH_ENDPOINT = "/api/v1/auth/**";
    private final static String COMMENT_ENDPOINT = "/api/v1/comments/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Настройка безопасности HTTP.
     * * @param http объект для настройки HTTP security
     *
     * @throws Exception выбрасывается в случае возникновения ошибки при настройке
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(AUTH_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasRole("USER").antMatchers(EXECUTOR_ENDPOINT).hasRole("EXECUTOR")
                .antMatchers(COMMENT_ENDPOINT).hasAnyRole("USER", "EXECUTOR").anyRequest().authenticated()
                .and().apply(new JwtConfigurer(jwtTokenProvider));
    }
}