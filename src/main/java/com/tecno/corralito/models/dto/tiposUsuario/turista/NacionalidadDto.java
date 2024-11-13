package com.tecno.corralito.models.dto.tiposUsuario.turista;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NacionalidadDto {
    private String codigo;
    private String descripcion;
}
