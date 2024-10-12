package com.tecno.corralito.models.entity.productoEspesifico;


import com.tecno.corralito.models.entity.productoGeneral.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @Column(name = "id_comentario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComentario;

    @Column(name = "descipcion")
    private String descipcion;

    @Column(name = "Valoracion")
    private int valoracion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;


}

