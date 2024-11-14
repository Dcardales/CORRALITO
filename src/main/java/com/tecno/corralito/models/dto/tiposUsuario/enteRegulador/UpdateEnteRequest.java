package com.tecno.corralito.models.dto.tiposUsuario.enteRegulador;


import com.tecno.corralito.models.entity.enums.TipoIdentificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateEnteRequest {

    @NotEmpty(message = "El tipo de identificación no puede estar vacío")
    private TipoIdentificacion tipoIdentificacion;

    @NotEmpty(message = "La identificación no puede estar vacía")
    private String identificacion;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;

    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    @Size(max = 50, message = "Los apellidos no pueden exceder los 50 caracteres")
    private String apellidos;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
}
