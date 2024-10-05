package com.tecno.corralito.models.dto.Auth;

import com.tecno.corralito.models.entity.enums.Genero;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthCreateTuristaRequest {

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotNull(message = "El género no puede estar vacío")
    private Genero genero;

    @NotBlank(message = "La nacionalidad no puede estar vacía")
    private String nacionalidad;

}
