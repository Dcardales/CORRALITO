package com.tecno.corralito.models.entity.productoGeneral;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcionCategoria;
}