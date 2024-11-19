package com.tecno.corralito.models.dto.tiposUsuario.comercio;


import com.tecno.corralito.models.dto.tiposUsuario.usuario.UsuarioDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateComercio {
    private Integer id;
    private String nombreComercio;
    private String nit;
    private String razonSocial;
    private String direccionComercio;
    private String telefono;
    private UsuarioDto usuario;
}
