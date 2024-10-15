package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoEspMapper {

    CreateProductoEsp toDto(ProductoEsp productoEsp);

    ProductoEsp toEntity(CreateProductoEsp createProductoEsp);

    List<CreateProductoEsp> toDtoList(List<ProductoEsp> productosEsp);
}
