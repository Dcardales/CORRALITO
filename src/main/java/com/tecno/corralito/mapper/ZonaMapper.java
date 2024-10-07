package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZonaMapper {

    @Mapping(source = "nombreZona", target = "nombreZona")
    Zona toEntity(ZonaDto dto);

    @Mapping(source = "nombreZona", target = "nombreZona")
    ZonaDto toDto(Zona entity);
}
