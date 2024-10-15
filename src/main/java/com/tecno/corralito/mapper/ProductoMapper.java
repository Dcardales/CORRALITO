package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoDto toDto(Producto producto);

    @Mapping(target = "categoria.idCategoria", source = "idCategoria")
    @Mapping(target = "zona.idZona", source = "idZona")
    Producto toEntity(ProductoDto productoDto);

    List<ProductoDto> toDtoList(List<Producto> productos);

}

