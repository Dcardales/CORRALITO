package com.tecno.corralito.models.entity.usuario;


import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.tiposUsuarios.Administrador;
import com.tecno.corralito.models.entity.tiposUsuarios.Comercio;
import com.tecno.corralito.models.entity.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.entity.tiposUsuarios.Turista;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;

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

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Administrador administrador;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Comercio comercio;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private EnteRegulador enteRegulador;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Turista turista;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();
}