package com.tecno.corralito.models.dto.productoEspecifico.productoEsp;



import com.tecno.corralito.models.entity.productoGeneral.Zona;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoEspSimple {

    private Integer idProductoEsp;
    private String nombreEspecifico;
    private Double precio;
    private String descripcion;
    private int valoracionPromedio;
    private String nombreComercio;
    private Zona zona;

}
