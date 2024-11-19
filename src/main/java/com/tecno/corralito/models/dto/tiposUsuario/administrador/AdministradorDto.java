package com.tecno.corralito.models.dto.tiposUsuario.administrador;



import com.tecno.corralito.models.dto.tiposUsuario.usuario.UsuarioDto;
import com.tecno.corralito.models.entity.enums.TipoIdentificacion;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdministradorDto {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private TipoIdentificacion tipoIdentificacion;
    private String identificacion;
    private UsuarioDto usuario;
}
