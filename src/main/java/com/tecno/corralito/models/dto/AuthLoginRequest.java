package com.tecno.ctgbank.models.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String email,
                               @NotBlank String password) {
}