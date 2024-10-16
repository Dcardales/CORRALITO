package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductoEspMapper {

    @Mapping(source = "productoEspExistenteDto.idProducto", target = "producto.idProducto")
    @Mapping(source = "productoEspExistenteDto.precio", target = "precio")
    @Mapping(source = "productoEspExistenteDto.descripcion", target = "descripcion")
    @Mapping(source = "productoEspExistenteDto.estado", target = "estado")
    @Mapping(source = "zona.idZona", target = "zona.idZona")  // Aquí corregimos
    ProductoEsp mapToEntity(ProductoEspExistenteDto productoEspExistenteDto, Producto producto, Zona zona);


    @Mapping(source = "producto.idProducto", target = "producto.idProducto")
    @Mapping(source = "productoEspExistenteDto.precio", target = "precio")
    @Mapping(source = "productoEspExistenteDto.descripcion", target = "descripcion")
    @Mapping(source = "productoEspExistenteDto.estado", target = "estado")
    @Mapping(source = "zona.idZona", target = "Zona.idZona")
    ProductoEsp toEntity(ProductoEspExistenteDto productoEspExistenteDto, Producto producto, Zona zona);

    @Mapping(source = "ProductoEspPersonalizadoDto.nombreProducto", target = "nombreProducto")
    @Mapping(source = "ProductoEspPersonalizadoDto.precio", target = "precio")
    @Mapping(source = "ProductoEspPersonalizadoDto.descripcion", target = "descripcion")
    @Mapping(source = "ProductoEspPersonalizadoDto.estado", target = "estado")
    @Mapping(source = "zona.idZona", target = "zona.idZona")
    ProductoEsp toEntityPersonalizado(ProductoEspPersonalizadoDto productoEspPersonalizadoDto, Categoria categoria, Zona zona, Comercio comercio);
}

