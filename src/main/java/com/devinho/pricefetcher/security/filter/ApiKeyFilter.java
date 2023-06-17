package com.devinho.pricefetcher.security.filter;

import com.devinho.pricefetcher.security.authentication.ApiKeyAuthentication;
import com.devinho.pricefetcher.security.manager.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${security-config.api-key-header}")
    private String apiKeyHeaderName;
    @Value("${security-config.api-key-value}")
    private String validApiKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String receivedApiKey = request.getHeader(apiKeyHeaderName);
        if (receivedApiKey == null || receivedApiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var apiKeyAuthentication = new ApiKeyAuthentication(false, receivedApiKey);
        var authManager = new CustomAuthenticationManager(validApiKey);
        try {
            var authenticationResult = authManager.authenticate(apiKeyAuthentication);
            if (authenticationResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
