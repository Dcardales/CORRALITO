package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.tiposUsuario.turista.ObtenerTurista;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TuristaMapper {

    @Mapping(target = "userEntity.password", ignore = true) // Ignora el campo "password"
    @Mapping(target = "userEntity.roles", ignore = true)   // Ignora los roles
    ObtenerTurista toObtenerTurista(Turista turista);
}
