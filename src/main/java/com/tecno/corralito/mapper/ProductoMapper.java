package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoDto toDto(Producto producto);

    Producto toEntity(ProductoDto productoDto);

    List<ProductoDto> toDtoList(List<Producto> productos);

}

