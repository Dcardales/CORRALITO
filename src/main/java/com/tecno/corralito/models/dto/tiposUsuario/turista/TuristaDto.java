package com.tecno.corralito.models.dto.tiposUsuario.turista;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TuristaDto {

    private String nombre;
    private String apellidos;
}
