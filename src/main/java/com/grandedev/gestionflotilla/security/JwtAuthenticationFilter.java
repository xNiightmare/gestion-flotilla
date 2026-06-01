package com.grandedev.gestionflotilla.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain
    ) throws ServletException, IOException {
        //Extracts and validates JWT from Authorization header
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(jwtService.validateToken(token)){
                String username = jwtService.getUserNameFromToken(token);
                String role = jwtService.getRoleFromToken(token);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(authority)
                );
                //Sets authentication in the security context if valid
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}

//A custom filter that intercepts requests, validates JWT tokens,
//and sets authentication in the security context.