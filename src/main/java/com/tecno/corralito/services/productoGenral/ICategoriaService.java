package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    List<Categoria> listAll();

    Categoria save(CategoriaDto perfil);

    Categoria findById(Integer id);

    void delete(Categoria perfil);

    boolean existsById(Integer id);

}


