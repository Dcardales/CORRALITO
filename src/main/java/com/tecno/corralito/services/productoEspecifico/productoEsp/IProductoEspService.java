package com.tecno.corralito.services.productoEspecifico.productoEsp;

import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspSimple;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoEspService {

    ProductoEsp obtenerProductoEspecifico(Integer idProductoEsp);

    List<ProductoEsp> listarProductosEspecificosComercio(Integer idComercio);

    Page<ProductoEsp> listarProductosPorCategoria(Integer idCategoria, Pageable pageable);

    Page<ProductoEsp> listarProductosPorCategoriaYZona(Integer idCategoria, Integer idZona, Pageable pageable);

    List<ProductoEsp> listarProductosMasRecientes();

    List<ProductoEsp> listarProductosPorMayorPrecio();

    List<ProductoEsp> listarProductosPorMenorPrecio();

    Page<ProductoEsp> listarTodosLosProductosEspecificos(Pageable pageable);

    void eliminarProductoEspecifico(Integer idProductoEsp);

    ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto);

    ProductoEsp actualizarProductoEsp(Integer idProductoEsp, ProductoEspPersonalizadoDto dto);

    List<ProductoEsp> listarProductosPorZona(Integer idZona);

    List<ProductoEspSimple> listarProductosPorZonaSimple(Integer idZona);
}