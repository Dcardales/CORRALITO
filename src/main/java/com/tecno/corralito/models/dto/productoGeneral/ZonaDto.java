package com.tecno.corralito.models.dto.productoGeneral;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZonaDto {

    private Integer idZona;

    @NotBlank(message = "El nombre de la zona no puede estar vacío")
    private String nombreZona;

    @NotBlank(message = "La descripcion de la zona no puede estar vacío")
    private String descripcionZona;
}
