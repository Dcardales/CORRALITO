package com.tecno.corralito.mapper;


import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspSimple;
import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
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

    // Mapeo de ProductoEsp a ProductoEspSimple
    @Mapping(target = "idProductoEsp", source = "idProductoEsp")
    @Mapping(target = "zona.idZona", source = "zona.idZona")
    @Mapping(target = "nombreEspecifico", source = "nombreEspecifico")
    @Mapping(target = "precio", source = "precio")
    @Mapping(target = "descripcion", source = "descripcion")
    @Mapping(target = "valoracionPromedio", expression = "java(calcularValoracionPromedio(productoEsp))")
    @Mapping(target = "nombreComercio", source = "comercio.nombreComercio")
    ProductoEspSimple toProductoEspSimple(ProductoEsp productoEsp);

    // Método auxiliar para calcular el promedio de valoraciones
    default int calcularValoracionPromedio(ProductoEsp productoEsp) {
        if (productoEsp.getComentarios() == null || productoEsp.getComentarios().isEmpty()) {
            return 0; // Sin valoraciones
        }
        // Sumar todas las valoraciones y calcular el promedio
        double promedio = productoEsp.getComentarios().stream()
                .mapToInt(Comentario::getValoracion)
                .average()
                .orElse(0.0); // Valor por defecto si no hay comentarios
        return (int) Math.round(promedio); // Redondear al entero más cercano
    }
}


