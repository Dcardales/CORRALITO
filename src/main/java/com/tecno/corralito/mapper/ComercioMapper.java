package com.tecno.corralito.mapper;


import com.tecno.corralito.models.dto.tiposUsuario.comercio.UpdateComercio;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface ComercioMapper {

    // Método para mapear Comercio a ComercioDto
    UpdateComercio toUpdateComercio(Comercio comercio);

    // Método opcional para mapear ComercioDto a Comercio si se requiere (para actualizaciones, por ejemplo)
    @InheritInverseConfiguration
    Comercio toComercio(UpdateComercio updateComercio);
}
