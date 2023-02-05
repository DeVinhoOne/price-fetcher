package com.devinho.pricefetcher.security.manager;

import com.devinho.pricefetcher.security.provider.ApiKeyAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final ApiKeyAuthenticationProvider apiKeyAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (apiKeyAuthenticationProvider.supports(authentication.getClass()))
            return apiKeyAuthenticationProvider.authenticate(authentication);
        throw new SecurityException("Authentication provider failure");
    }
}
