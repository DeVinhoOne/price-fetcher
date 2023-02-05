package com.devinho.pricefetcher.security.provider;

import com.devinho.pricefetcher.security.authentication.ApiKeyAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Value("${security-config.api-key-value}")
    private String apiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiKeyAuthentication = (ApiKeyAuthentication) authentication;
        var headerApiKey = apiKeyAuthentication.getHeaderApiKey();

        if (headerApiKey.equals(apiKey)) return new ApiKeyAuthentication(true, null);
        throw new BadCredentialsException("Valid API-KEY is required");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.equals(authentication);
    }
}
