package com.devinho.pricefetcher.security.provider;

import com.devinho.pricefetcher.security.authentication.ApiKeyAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final String validApiKey;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiKeyAuthentication = (ApiKeyAuthentication) authentication;
        var headerApiKey = apiKeyAuthentication.getHeaderApiKey();
        if (headerApiKey.equals(validApiKey)) return new ApiKeyAuthentication(true, headerApiKey);
        throw new BadCredentialsException("Valid API-KEY is required");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.equals(authentication);
    }
}
