package com.tecno.corralito.services.impl;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tecno.corralito.services.IJwtService;
import com.tecno.corralito.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements IJwtService {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtServiceImpl(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createToken(Authentication authentication) {
        return jwtUtils.createToken(authentication);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(decodedJWT);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return userDetails.getUsername().equals(username) && !decodedJWT.getExpiresAt().before(new Date());
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    @Override
    public String extractUsernameFromToken(String token) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        return jwtUtils.extractUsername(decodedJWT);
    }

    @Override
    public List<String> extractAuthoritiesFromToken(String token) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();
        return Arrays.asList(authorities.split(","));
    }
}


