package com.tecno.corralito.models.entity.productoEspesifico;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "prod_esp")
public class ProductoEspecifico implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prod_esp")
    private Integer idProductoEspecifico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_general_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Producto producto;

    @Column(name = "precio_comercio", precision = 10, scale = 3)
    private BigDecimal precio;

    @Column(name = "direccion_comercio")
    private String direccionComercio;

    @Column(name = "descripcion_comercio")
    private String descripcion;

    // Relación con Comercio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comercio_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Comercio comercio;

    // Otros atributos específicos

    // Getters y Setters
}
