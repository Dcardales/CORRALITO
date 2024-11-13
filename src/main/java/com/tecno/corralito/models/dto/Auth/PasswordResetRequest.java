package com.tecno.corralito.models.dto.Auth;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PasswordResetRequest {
    @NotEmpty(message = "La nueva contraseña no puede estar vacía")
    private String newPassword;

}
