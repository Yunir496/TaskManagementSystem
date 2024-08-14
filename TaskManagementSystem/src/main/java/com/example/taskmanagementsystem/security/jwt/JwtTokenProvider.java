package com.example.taskmanagementsystem.security.jwt;

import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.exception.JwtAuthenticationsException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Провайдер токенов JWT для аутентификации и авторизации.
 */
@Component
public class JwtTokenProvider {
    @Value("${jwt.token.secret:querty}")
    private String secret;
    @Value("${jwt.token.expired:3600000}")
    private Long expired;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Создает JWT токен на основе адреса электронной почты пользователя и списка ролей.
     *
     * @param email адрес электронной почты пользователя
     * @param roles список ролей пользователя
     * @return сформированный JWT токен
     */
    public String createToken(String email, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", getRoleName(roles));
        Date now = new Date();
        Date validity = new Date(now.getTime() + expired);
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity).signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * Получает аутентификацию пользователя из JWT токена.
     *  @param token JWT токен для извлечения аутентификации
     *
     * @return объект аутентификации пользователя
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Извлекает адрес электронной почты пользователя из JWT токена.
     *  @param token JWT токен для извлечения адреса электронной почты
     *
     * @return адрес электронной почты пользователя
     */
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Разрешает JWT токен из запроса.
     *  @param rec HTTP запрос для извлечения JWT токена
     *
     * @return строку JWT токена или null
     */
    public String resolveToken(HttpServletRequest rec) {
        String bearerToken = rec.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Проверяет валидность JWT токена.
     *  @param token JWT токен для проверки
     *
     * @return true, если токен валидный, иначе false
     * @throws JwtAuthenticationsException если токен просрочен или неверный
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationsException("Jwt token is expired or invalid");
        }
    }

    /**
     * Получает список имен ролей пользователей.
     *
     * @param roles список ролей пользователя
     *
     * @return список имен ролей
     */
    private List<String> getRoleName(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }
}