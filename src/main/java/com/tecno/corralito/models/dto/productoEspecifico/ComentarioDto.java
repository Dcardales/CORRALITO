package com.tecno.corralito.models.dto.productoEspecifico;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ComentarioDto implements Serializable {

    private Integer idComentario;

    private String descripcion;

    @Min(value = 1, message = "La valoración mínima es 1")
    @Max(value = 5, message = "La valoración máxima es 5")
    private int valoracion;

    private String nombreTurista;

    private LocalDateTime fecha;

}
