package com.tecno.corralito.models.dto.productoEspecifico;

import com.tecno.corralito.models.entity.enums.Estado;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateProductoEsp {

    private Integer productoId;

    @NotNull
    @Size(min = 3, max = 100)
    private String nombreEspecifico;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotNull
    private String descripcion;

    private Estado estado;

    private Integer comercioId;

    private Integer zonaId;

    private List<ComentarioDto> comentarios;

}

