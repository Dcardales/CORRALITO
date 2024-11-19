package com.tecno.corralito.models.entity.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Nacionalidad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Nacionalidad {

    @Id
    @Column(name = "id_nacionalidad")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNacionalidad;

    @Column(name = "codigo", unique = true)
    private String codigo;

    @Column(name = "descripcion")
    private String descripcion;


    public Nacionalidad(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
}