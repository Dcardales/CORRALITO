package com.tecno.corralito.models.dto.productoEspecifico;

import com.tecno.corralito.models.entity.enums.Estado;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoEspExistenteDto {

    private Integer idProducto;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal precio;

    @NotNull
    private String descripcion;

    private Estado estado;

    private Integer idZona;

}
