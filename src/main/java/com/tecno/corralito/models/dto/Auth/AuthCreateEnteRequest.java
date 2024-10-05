package com.tecno.corralito.models.dto.Auth;

import com.tecno.corralito.models.entity.enums.TipoIdentificacion;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthCreateEnteRequest {

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotNull(message = "Seleccione un tipo de identificacio valido")
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "El numero de identificacion no puede estar vacío")
    private String identificacion;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
}
