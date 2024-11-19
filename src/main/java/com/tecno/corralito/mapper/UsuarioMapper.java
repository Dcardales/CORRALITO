package com.tecno.corralito.mapper;


import com.tecno.corralito.models.dto.tiposUsuario.usuario.UsuarioDto;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Mapea UserEntity a UsuarioDto, ignorando campos que no están en UsuarioDto
    @Mapping(target = "userEntity.password", ignore = true) // Esto ignora el campo "password"
    UsuarioDto toUsuarioDto(UserEntity userEntity);
}


