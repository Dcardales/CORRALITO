package com.tecno.corralito.models.response.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"username", "message", "status", "jwt"})
public record AuthResponse(
        String email,
        String message,
        String jwt,
        Boolean status) {
}
