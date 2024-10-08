package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;

import java.util.List;

public interface IProductoService {

    ProductoDto crearProducto(ProductoDto productoDto);

    ProductoDto actualizarProducto(Integer id, ProductoDto productoDto);

    void eliminarProducto(Integer id);

    List<ProductoDto> listarProductos();

    ProductoDto buscarProductoPorNombre(String nombreProducto);
}
