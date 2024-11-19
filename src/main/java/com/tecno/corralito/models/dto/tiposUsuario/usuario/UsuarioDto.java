package com.tecno.corralito.models.dto.tiposUsuario.usuario;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UsuarioDto {
    private Long id;
    private String email;
    private String estado;
}

