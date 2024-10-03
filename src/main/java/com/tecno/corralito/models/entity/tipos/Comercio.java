package com.tecno.ctgbank.models.entity.tipos;

import com.tecno.ctgbank.models.entity.usuario.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "comercio")
public class Comercio implements Serializable {

    @Id
    @Column(name = "id_comercio")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_comercio", nullable = false)
    @NotEmpty(message = "El nombre del comercio no puede estar vacío")
    private String nombreComercio;

    @Column(name = "nit", nullable = false, unique = true)
    @NotEmpty(message = "El NIT no puede estar vacío")
    private String nit;

    @Column(name = "razon_social", length = 100)
    private String razonSocial;

    @Column(name = "direccion_comercio", length = 200)
    private String direccionComercio;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "tipo_comercio", length = 50)
    private String tipoComercio;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UserEntity usuario;
}