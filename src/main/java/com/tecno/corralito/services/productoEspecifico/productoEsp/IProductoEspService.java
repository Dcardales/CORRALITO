package com.tecno.corralito.services.productoEspecifico.productoEsp;

import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;

import java.util.List;

public interface IProductoEspService {

    ProductoEsp obtenerProductoEspecifico(Integer idProductoEsp);

    List<ProductoEsp> listarProductosEspecificosComercio(Integer idComercio);

    List<ProductoEsp> listarTodosLosProductosEspecificos();

    void eliminarProductoEspecifico(Integer idProductoEsp);

    ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto);

    ProductoEsp actualizarProductoEsp(Integer idProductoEsp, ProductoEspPersonalizadoDto dto);

}