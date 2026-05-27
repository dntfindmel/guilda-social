package com.guildasocial.security;

public class SecurityConstants {
    public static final String AUTH_WHITELIST[] = {
        "/api/auth/**",
        "/api/usuarios",
        "/swagger-ui/**",
        "/api-docs/**",
        "/v3/api-docs/**"
    };
}
