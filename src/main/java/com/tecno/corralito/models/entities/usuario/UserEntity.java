package com.tecno.corralito.models.entities.usuario;

import com.tecno.corralito.models.entities.enums.Estado;
import com.tecno.corralito.models.entities.tiposUsuarios.Administrador;
import com.tecno.corralito.models.entities.tiposUsuarios.Comercio;
import com.tecno.corralito.models.entities.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.entities.tiposUsuarios.Turista;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class
UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correo", nullable = false, unique = true)
    @Email(message = "El correo no tiene un formato válido")
    @NotEmpty(message = "El correo no puede estar vacío")
    private String correo;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private Administrador administrador;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private Comercio comercio;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private EnteRegulador enteRegulador;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    private Turista turista;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();
}