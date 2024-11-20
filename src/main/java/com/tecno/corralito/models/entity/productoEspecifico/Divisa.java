package com.tecno.corralito.models.entity.productoEspecifico;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "divisas")
public class Divisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_divisa")
    private Long idDivisa;

    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo; // Ej: USD, EUR, etc.

    @Column(name = "nombre", nullable = false)
    private String nombre; // Ej: Dólar estadounidense, Euro, etc.

    @Column(name = "tasa_conversion", precision = 10, scale = 6, nullable = false)
    private BigDecimal tasaConversion; // Tasa de cambio respecto a COP.

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDate fechaActualizacion; // Fecha de la última actualización.


    public BigDecimal convertir(BigDecimal precioEnCOP) {
        if (precioEnCOP == null || precioEnCOP.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        // Realiza la conversión usando la tasa de conversión
        return precioEnCOP.divide(tasaConversion, 2, RoundingMode.HALF_UP);


    }
}