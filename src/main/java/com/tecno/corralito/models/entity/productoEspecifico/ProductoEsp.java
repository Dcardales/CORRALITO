package com.tecno.corralito.models.entity.productoEspecifico;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "prod_esp")
public class ProductoEsp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prod_esp")
    private Integer idProductoEsp;


    @Column(name = "nombreEsp")
    private String nombreEspecifico;

    @Column(name = "precio_esp", precision = 10, scale = 3)
    private BigDecimal precio;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comercio")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Comercio comercio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @OneToMany(mappedBy = "productoEsp", cascade = CascadeType.ALL)
    private List<Comentario> comentarios;

}


