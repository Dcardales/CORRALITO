package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;

import java.util.List;


public interface ICategoriaService {

    CategoriaDto crearCategoria(CategoriaDto categoriaDto);

    CategoriaDto actualizarCategoria(Integer idCategoria, CategoriaDto categoriaDto);

    void eliminarCategoria(Integer idCategoria);

    List<CategoriaDto> listarCategorias();

    CategoriaDto obtenerCategoriaPorId(Integer idCategoria);

    CategoriaDto obtenerCategoriaPorNombre(String nombreCategoria);
}


