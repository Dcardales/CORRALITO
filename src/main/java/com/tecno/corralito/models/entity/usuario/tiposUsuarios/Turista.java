package com.tecno.corralito.models.entity.usuario.tiposUsuarios;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tecno.corralito.models.entity.enums.Genero;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "turistas")
public class Turista {

    @Id
    @Column(name = "id_tuista")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "nacionalidad")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Nacionalidad nacionalidad;

    @Column(name = "nombre", length = 100, nullable = false)
    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @Column(name = "apellidos", length = 100, nullable = false)
    @NotEmpty(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UserEntity usuario;
}





