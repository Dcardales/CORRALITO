package com.tecno.corralito.mapper;

import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    CategoriaDto toDto(Categoria categoria);
    Categoria toEntity(CategoriaDto categoriaDto);
    List<CategoriaDto> toDtoList(List<Categoria> categorias);
}