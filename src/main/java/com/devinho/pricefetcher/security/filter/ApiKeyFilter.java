package com.devinho.pricefetcher.security.filter;

import com.devinho.pricefetcher.security.authentication.ApiKeyAuthentication;
import com.devinho.pricefetcher.security.manager.CustomAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${security-config.api-key-header}")
    private String apiKeyHeaderName;

    private final CustomAuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String receivedApiKey = Objects.requireNonNullElse(request.getHeader(apiKeyHeaderName), "");
        var apiKeyAuthentication = new ApiKeyAuthentication(false, receivedApiKey);
        var authenticationResult = authenticationManager.authenticate(apiKeyAuthentication);
        if (authenticationResult.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            filterChain.doFilter(request, response);
        }
    }
}
