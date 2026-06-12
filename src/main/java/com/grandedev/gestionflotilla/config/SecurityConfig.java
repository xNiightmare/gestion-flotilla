package com.grandedev.gestionflotilla.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // Enforced by Spring Security to trigger @PreAuthorize
public class SecurityConfig {

    private static final String[] ADMIN_LIST_URL = {"/api/v1/camiones/**",
            "/api/v1/documentos/**",
            "/api/v1/operadores/**",
            "/api/v1/usuarios/**"};
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .csrf(AbstractHttpConfigurer::disable) //Se cambio csrf -> csrf.disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers(POST, ADMIN_LIST_URL).hasRole("ADMIN")
                        .requestMatchers(PUT, ADMIN_LIST_URL).hasRole("ADMIN")
                        .requestMatchers(DELETE,ADMIN_LIST_URL).hasRole("ADMIN")
                        .requestMatchers(GET, ADMIN_LIST_URL).hasRole("ADMIN")
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

//spring security @preauthorize
//spring security @preauthorize by id