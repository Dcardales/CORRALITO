package com.tecno.corralito.models.request.Auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String email,
                                    @NotBlank String password,
                                    @NotBlank String estado,
                                    @Valid AuthCreateRoleRequest roleRequest) {
}
