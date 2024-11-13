package com.tecno.corralito.models.dto.tiposUsuario.turista;

import com.tecno.corralito.models.entity.enums.Genero;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TuristaUpdateRequest {

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    private Genero genero;

    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 caracteres")
    private String telefono;

    private String nacionalidad;


}
