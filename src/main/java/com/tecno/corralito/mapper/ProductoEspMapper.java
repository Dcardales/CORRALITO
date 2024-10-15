package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoEspMapper {

    CreateProductoEsp toDto(ProductoEsp productoEsp);

    ProductoEsp toEntity(CreateProductoEsp createProductoEsp);

    List<CreateProductoEsp> toDtoList(List<ProductoEsp> productosEsp);


    @Mapping(source = "producto.idProducto", target = "producto.idProducto")
    @Mapping(source = "precio", target = "precio")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "zona", target = "zona.idZona")
    ProductoEsp toEntity(ProductoEspExistenteDto dto, Producto producto, Zona zona);

    @Mapping(source = "nombreProducto", target = "producto.nombreProducto")
    @Mapping(source = "precio", target = "precio")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "zona", target = "zona.idZona")
    ProductoEsp toEntityPersonalizado(ProductoEspPersonalizadoDto dto, Categoria categoria, Zona zona, Comercio comercio);
}

