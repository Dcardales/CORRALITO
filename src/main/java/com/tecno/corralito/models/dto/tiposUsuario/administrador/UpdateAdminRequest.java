package com.tecno.corralito.models.dto.tiposUsuario.administrador;

import com.tecno.corralito.models.entity.enums.TipoIdentificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateAdminRequest {

    @NotNull(message = "Seleccione un tipo de identificacio valido")
    private TipoIdentificacion tipoIdentificacion;

    @NotBlank(message = "La identificación es obligatoria.")
    private String identificacion;

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios.")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio.")
    private String telefono;
}
