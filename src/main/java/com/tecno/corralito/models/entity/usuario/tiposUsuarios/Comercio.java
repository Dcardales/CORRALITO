package com.tecno.corralito.models.entity.usuario.tiposUsuarios;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "comercio")
public class Comercio {

    @Id
    @Column(name = "id_comercio")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_comercio", nullable = false)
    private String nombreComercio;

    @Column(name = "nit", nullable = false, unique = true)
    private String nit;

    @Column(name = "razon_social", length = 100)
    private String razonSocial;

    @Column(name = "direccion_comercio", length = 200)
    private String direccionComercio;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UserEntity usuario;
}