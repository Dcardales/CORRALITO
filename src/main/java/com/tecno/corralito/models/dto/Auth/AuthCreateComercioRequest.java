package com.tecno.corralito.models.dto.Auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthCreateComercioRequest {

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "El nombre del comercio no puede estar vacío")
    private String nombreComercio;

    @NotBlank(message = "El nit no puede estar vacio")
    private String nit;

    @NotBlank(message = "La Razon Social no puede estar vacia")
    private String razonSocial;

    @NotBlank(message = "La direccion no puede estar vacía")
    private String direccionComercio;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

}

