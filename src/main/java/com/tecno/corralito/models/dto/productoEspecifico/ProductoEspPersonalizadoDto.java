package com.tecno.corralito.models.dto.productoEspecifico;

import com.tecno.corralito.models.entity.enums.Estado;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoEspPersonalizadoDto {

    @NotNull
    @Size(min = 3, max = 100)
    private String nombreProducto;

    private Integer idCategoria;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotNull
    private String descripcion;

    private Estado estado;

    private Integer idZona;

}
