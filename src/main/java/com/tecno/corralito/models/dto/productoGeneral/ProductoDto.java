package com.tecno.corralito.models.dto.productoGeneral;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private Integer idProducto;

    @NotNull(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del producto debe tener entre 2 y 100 caracteres")
    private String nombreProducto;

    @NotNull(message = "El precio mínimo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio mínimo debe ser mayor que 0")
    private BigDecimal precioMin;

    @NotNull(message = "El precio máximo es obligatorio")
    @DecimalMax(value = "99000000000000", message = "El precio máximo no debe superar los 999.999.999.999.00")
    private BigDecimal precioMax;

    private CategoriaDto categoria;
    private ZonaDto zona;
}
