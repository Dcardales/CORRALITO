package com.tecno.corralito.models.entities.tiposUsuarios;

import com.tecno.corralito.models.entities.enums.TipoIdentificacion;
import com.tecno.corralito.models.entities.usuario.UserEntity;
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
@Table(name = "ente_regulador")
public class EnteRegulador implements Serializable {

    @Id
    @Column(name = "id_ente")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_identificacion")
    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @Column(name = "nombre", nullable = false, length = 50)
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 50)
    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UserEntity userEntity;

}

