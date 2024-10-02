package com.tecno.corralito.models.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private Double saldo;
}
