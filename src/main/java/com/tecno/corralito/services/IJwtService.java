package com.tecno.corralito.services;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface IJwtService {

    String createToken(Authentication authentication);

    boolean isTokenValid(String token);

    String extractUsernameFromToken(String token);

    List<String> extractAuthoritiesFromToken(String token);
}
