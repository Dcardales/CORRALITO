package com.tecno.corralito.models.dto.tiposUsuarios;

import com.tecno.corralito.models.entity.enums.Genero;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TuristaDto {
    @NotEmpty(message = "El email no puede estar vacío")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @NotEmpty(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotNull(message = "El género no puede estar vacío")
    private Genero genero;

    @NotNull(message = "La nacionalidad no puede estar vacía")
    private String nacionalidad;

}
