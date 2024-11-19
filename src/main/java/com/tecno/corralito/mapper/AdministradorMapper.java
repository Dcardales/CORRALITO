package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.tiposUsuario.administrador.AdministradorDto;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministradorMapper {

    @Mapping(target = "userEntity.password", ignore = true) // Ignora el campo "password"
    @Mapping(target = "userEntity.roles", ignore = true)   // Ignora los roles
    AdministradorDto toAdministradorDto(Administrador administrador);
}
