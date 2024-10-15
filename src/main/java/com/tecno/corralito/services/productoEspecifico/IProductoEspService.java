package com.tecno.corralito.services.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;

import java.util.List;

public interface IProductoEspService {

    CreateProductoEsp obtenerProductoEspecifico(Integer idProductoEsp);

    void eliminarProductoEspecifico(Integer idProductoEsp);

    List<CreateProductoEsp> listarProductosEspecificos();


    ProductoEsp crearProductoEspExistente(ProductoEspExistenteDto dto, Integer idComercio);

    ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto, Integer idComercio);
}