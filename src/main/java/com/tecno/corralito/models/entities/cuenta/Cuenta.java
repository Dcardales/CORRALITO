package com.tecno.corralito.models.entities.cuenta;

import com.tecno.corralito.models.entities.usuario.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true)
    @NotEmpty(message = "el numero de cuenta no puede estar vacío")
    private String numeroCuenta;

    @Min(value = 0, message = "El saldo no puede ser negativo")
    @Column(name = "saldo", nullable = false)
    private double saldo;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UserEntity usuario;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaccion> transacciones;
}
