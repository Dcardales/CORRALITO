package com.tecno.corralito.services.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;

import java.util.List;

public interface IProductoEspService {

    CreateProductoEsp obtenerProductoEspecifico(Integer idProductoEsp);

    CreateProductoEsp crearProductoEspecifico(CreateProductoEsp createProductoEsp);

    void eliminarProductoEspecifico(Integer idProductoEsp);

    List<CreateProductoEsp> listarProductosEspecificos();


}