package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;

import java.util.List;

public interface ICategoriaService {

    List<Categoria> listAll();

    Categoria save(CategoriaDto perfil);

    Categoria findById(Integer id);

    void delete(Categoria perfil);

    boolean existsById(Integer id);

    public default CategoriaDto convertirCategoriaACategoriaDto(Categoria categoria) {
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setIdCategoia(categoria.getIdCategoia());
        categoriaDto.setNombreCategoria(categoria.getNombreCategoria());
        return categoriaDto;
    }
}


