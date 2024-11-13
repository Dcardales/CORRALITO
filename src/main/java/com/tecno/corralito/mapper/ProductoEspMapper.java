package com.tecno.corralito.mapper;


import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.response.productoEspesifico.productoEsp.ProductoEspResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ProductoEspMapper {

    ProductoEspMapper INSTANCE = Mappers.getMapper(ProductoEspMapper.class);

    // Mapeo de ProductoEsp a ProductoEspResponse
    @Mapping(target = "comercio.id", source = "comercio.id")
    @Mapping(target = "comercio.nombreComercio", source = "comercio.nombreComercio")
    @Mapping(target = "zona.idZona", source = "zona.idZona")
    @Mapping(target = "zona.nombreZona", source = "zona.nombreZona")
    @Mapping(target = "categoria.idCategoria", source = "categoria.idCategoria")
    @Mapping(target = "categoria.nombreCategoria", source = "categoria.nombreCategoria")
    ProductoEspResponse toProductoEspResponse(ProductoEsp productoEsp);

    // Mapeo del DTO a la entidad ProductoEsp
    @Mapping(target = "nombreEspecifico", source = "nombreProducto")
    ProductoEsp toProductoEsp(ProductoEspPersonalizadoDto dto);

    // Mapeo inverso para convertir la entidad a DTO
    @Mapping(target = "nombreProducto", source = "nombreEspecifico")
    @Mapping(target = "idZona", source = "zona.idZona")
    @Mapping(target = "idCategoria", source = "categoria.idCategoria")
    ProductoEspPersonalizadoDto toDto(ProductoEsp productoEsp);
}

