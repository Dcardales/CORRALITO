package com.tecno.corralito.models.dto.authDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"correo", "message", "status", "jwt"})
public record AuthResponse(
        String correo,
        String message,
        String jwt,
        Boolean status) {
}
