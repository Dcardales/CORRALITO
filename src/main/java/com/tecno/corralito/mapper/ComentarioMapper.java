package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    @Mapping(target = "nombreTurista.nombre", source = "turista.nombre") // Asegúrate de mapear el nombre
    @Mapping(target = "nombreTurista.apellidos", source = "turista.apellidos")
    ComentarioDto toDto(Comentario comentario);

    Comentario toEntity(ComentarioDto comentarioDto);

    // Mapea el comentario junto con el turista (cuando sea necesario)
    @Mapping(target = "nombreTurista.nombre", source = "turista.nombre")
    @Mapping(target = "nombreTurista.apellidos", source = "turista.apellidos")
    ComentarioDto toDtoWithTurista(Comentario comentario);
}
