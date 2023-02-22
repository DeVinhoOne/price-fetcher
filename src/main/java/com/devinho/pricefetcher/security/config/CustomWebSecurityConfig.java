package com.devinho.pricefetcher.security.config;

import com.devinho.pricefetcher.security.filter.ApiKeyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomWebSecurityConfig {

    @Value("${security-config.api-key-value}")
    private String validApiKey;
    private final ApiKeyFilter apiKeyFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Security loaded with API-KEY: {}...", validApiKey.substring(0, 7));
        return http
                .addFilterAt(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .build();
    }
}
