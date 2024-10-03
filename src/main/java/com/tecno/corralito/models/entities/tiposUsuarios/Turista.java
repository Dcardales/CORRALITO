package com.tecno.corralito.models.entities.tiposUsuarios;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecno.corralito.models.entities.enums.Genero;
import com.tecno.corralito.models.entities.usuario.Nacionalidad;
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
@Table(name = "turistas")
public class Turista implements Serializable {

    @Id
    @Column(name = "id_tuista")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "nacionalidad")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Nacionalidad nacionalidad;

    @Column(name = "nombre", nullable = false)
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Column(name = "genero", nullable = false)
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UserEntity userEntity;

}





