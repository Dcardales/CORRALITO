package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.EnteReguladorDto;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnteReguladorMapper {

    @Mapping(target = "userEntity.password", ignore = true) // Ignora el campo "password"
    @Mapping(target = "userEntity.roles", ignore = true)   // Ignora los roles
    EnteReguladorDto toEnteReguladorDto(EnteRegulador enteRegulador);
}
