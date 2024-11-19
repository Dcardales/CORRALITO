package com.tecno.corralito.models.dto.tiposUsuario.turista;


import com.tecno.corralito.models.dto.tiposUsuario.usuario.UsuarioDto;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObtenerTurista {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private Nacionalidad nacionalidad;
    private UsuarioDto usuario;
}

