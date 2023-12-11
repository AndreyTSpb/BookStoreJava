package com.example.bookstore.filter;

import com.example.bookstore.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

@Component
//@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    /**
     *     "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJib29rc3RvcmUiLCJzdWIiOiJjbGllbnRfaWQiLCJpc3MiOiJhdXRoLXNlcnZpY2UiLCJleHAiOjE3MDIyODkxNzYsImlhdCI6MTcwMjI4ODg3Nn0.dJO8xmbwEoES-II8aRm8Dw6Nyk1vThpG8wjyPk07XsU"
     */

    @Value("${auth.enabled}")
    private boolean enabled;

    public AuthorizationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if(!enabled){
            filterChain.doFilter(request, response);
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || authHeader.isBlank()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if(!checkAuthorization(authHeader)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(request, response);
        }

    }

    private boolean checkAuthorization(String auth) {
        if (!auth.startsWith("Bearer "))
            return false;

        String token = auth.substring(7);
        return tokenService.checkToken(token);
    }
}
