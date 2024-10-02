package com.tecno.corralito.models.dto;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransferenciaDTO {
    private String numeroCuentaDestino;
    private double monto;
    private LocalDateTime fecha;
}
