package com.tecno.corralito.models.dto.authDTO;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String correo,
                               @NotBlank String password) {
}
