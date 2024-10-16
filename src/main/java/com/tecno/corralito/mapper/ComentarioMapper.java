package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    ComentarioDto toDto(Comentario comentario);

    Comentario toEntity(ComentarioDto comentarioDto);

    @Mapping(target = "nombreTurista", source = "turista.nombre")
    ComentarioDto toDtoWithTurista(Comentario comentario);
}
