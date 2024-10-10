package com.tecno.corralito.models.entity.productoGeneral;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "zonas")
public class Zona implements Serializable {

    @Id
    @Column(name = "id_zona")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idZona;

    @Column(name = "nombre_zona")
    private String nombreZona;

    @Column(name = "descripcion")
    private String descripcionZona;
}