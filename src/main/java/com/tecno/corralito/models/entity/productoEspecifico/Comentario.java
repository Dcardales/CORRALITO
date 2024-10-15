package com.tecno.corralito.models.entity.productoEspecifico;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "comentario")
public class Comentario implements Serializable {

    @Id
    @Column(name = "id_comentario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComentario;

    @Column(name = "valoracion")
    private int valoracion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_prod_esp")
    private ProductoEsp productoEsp;

    @ManyToOne
    @JoinColumn(name = "id_turista")
    private Turista turista;

}

