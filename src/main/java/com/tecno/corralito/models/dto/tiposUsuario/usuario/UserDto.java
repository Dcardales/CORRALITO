package com.tecno.corralito.models.dto.tiposUsuario.usuario;


import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private Long id;
    private String email;
    private Set<String> perfiles;


}
