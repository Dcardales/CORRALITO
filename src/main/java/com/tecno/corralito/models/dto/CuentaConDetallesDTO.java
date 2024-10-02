package com.tecno.corralito.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaConDetallesDTO {
    private Long id;
    private String numeroCuenta;
    private Double saldo;
    private String username;
    private List<TransferenciaDTO> transacciones;
}