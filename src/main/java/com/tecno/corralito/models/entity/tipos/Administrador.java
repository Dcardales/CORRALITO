package com.tecno.ctgbank.models.entity.tipos;


import com.tecno.ctgbank.models.entity.enums.TipoIdentificacion;
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
@Table(name = "administrador")
public class Administrador implements Serializable {

    @Id
    @Column(name = "id_admin")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_identificacion", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Column(name = "nombre", nullable = false)
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UserEntity usuario;
}