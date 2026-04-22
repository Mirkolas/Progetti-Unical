package it.unical.demacs.informatica.easyhomebackend.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabilita CSRF
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/open/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN" )
                        .requestMatchers("/api/auth/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/login")
                        .successHandler((req, res, auth) -> {

                            String username = auth.getName();
                            String role = auth.getAuthorities().stream()
                                    .map(authority -> authority.getAuthority())
                                    .findFirst()
                                    .orElse("ROLE_USER");

                            String jsonResponse = String.format("{\"username\": \"%s\", \"role\": \"%s\"}", username, role);
                            res.setStatus(200);
                            res.setContentType("application/json");
                            res.getWriter().write(jsonResponse);
                        })
                        .failureHandler((req, res, ex) -> {
                            ex.printStackTrace();
                            res.setStatus(401);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\": \"Credenziali errate.\"}");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessHandler((req, res, auth) -> {

                            req.getSession().invalidate();
                            res.addHeader("Set-Cookie", "JSESSIONID=; Path=/; HttpOnly; Max-Age=0");
                            res.setStatus(200);
                            res.getWriter().write("{\"message\": \"Logout effettuato con successo\"}");
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/api/login")
                        .maximumSessions(1)
                        .expiredUrl("/api/login?expired=true")
                );

        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}