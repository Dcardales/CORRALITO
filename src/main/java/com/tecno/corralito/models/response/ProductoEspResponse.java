package com.tecno.corralito.models.response;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.enums.Estado;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoEspResponse implements Serializable {

    private Integer idProductoEsp;
    private String nombreEspecifico;
    private BigDecimal precio;
    private String descripcion;
    private Estado estado;
    private ComercioResponseDto comercio;
    private ZonaDto zona;
    private CategoriaDto categoria;
    private List<ComentarioDto> comentarios;

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}

