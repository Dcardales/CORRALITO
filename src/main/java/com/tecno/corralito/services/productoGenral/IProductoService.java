package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IProductoService {

    List<ProductoDto> listAll();

    ProductoDto save(@Valid ProductoDto productoDto);

    ProductoDto findById(Integer id);

    void delete(Integer id);

    boolean existsById(Integer id);
}
