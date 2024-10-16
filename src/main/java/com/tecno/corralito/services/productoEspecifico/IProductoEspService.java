package com.tecno.corralito.services.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;

import java.util.List;

public interface IProductoEspService {


    ProductoEsp obtenerProductoEspecifico(Integer idProductoEsp);

    List<ProductoEsp> listarProductosEspecificos(Integer idComercio);

    void eliminarProductoEspecifico(Integer idProductoEsp);

    ProductoEsp crearProductoEspExistente(ProductoEspExistenteDto dto, Integer idComercio);

    ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto, Integer idComercio);
}