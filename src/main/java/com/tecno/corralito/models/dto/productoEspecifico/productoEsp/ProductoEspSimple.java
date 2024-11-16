package com.tecno.corralito.models.dto.productoEspecifico.productoEsp;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoEspSimple {

    private String nombreEspecifico;
    private Double precio;
    private String descripcion;
    private int valoracionPromedio;
    private String nombreComercio;

}
