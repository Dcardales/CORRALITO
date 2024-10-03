package com.tecno.ctgbank.models.entity.usuario;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Nacionalidad")
public class Nacionalidad implements Serializable {

    @Id
    @Column(name = "id_nacionalidad")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNacionalidad;

    @Column(name = "codigo", unique = true)
    private String codigo; // Ej. "US", "CO", etc.

    @Column(name = "descripcion")
    private String descripcion; // Ej. "United States", "Colombia", etc.

}