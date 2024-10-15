package com.tecno.corralito.services.productoEspecifico.impl;

import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.repository.productoEspecifico.ProductoEspRepository;
import com.tecno.corralito.models.repository.productoGeneral.ProductoRepository;
import com.tecno.corralito.services.productoEspecifico.IProductoEspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoEspServiceImpl implements IProductoEspService {

    @Autowired
    private ProductoEspRepository productoEspRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoEspMapper productoEspMapper;

    @Override
    public CreateProductoEsp obtenerProductoEspecifico(Integer idProductoEsp) {
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto especifico no encontrado"));
        return productoEspMapper.toDto(productoEsp);
    }

    @Override
    public List<CreateProductoEsp> listarProductosEspecificos() {
        return productoEspMapper.toDtoList(productoEspRepository.findAll());
    }

    @Override
    public CreateProductoEsp crearProductoEspecifico(CreateProductoEsp createProductoEsp) {
        // Buscar el producto general asociado
        Producto producto = productoRepository.findById(createProductoEsp.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado."));

        // Mostrar el rango de precios
        mostrarRangoPrecios(producto);

        // Mapear el DTO a la entidad
        ProductoEsp productoEsp = productoEspMapper.toEntity(createProductoEsp);

        // Asociar producto general
        productoEsp.setProducto(producto);

        // Guardar el producto específico
        return productoEspMapper.toDto(productoEspRepository.save(productoEsp));
    }

    // Método para mostrar el rango de precios
    private void mostrarRangoPrecios(Producto producto) {
        BigDecimal precioMin = producto.getPrecioMin();
        BigDecimal precioMax = producto.getPrecioMax();
        System.out.println("Rango de precios para el producto " + producto.getNombreProducto() + ": " +
                "Precio Mínimo = " + precioMin + ", Precio Máximo = " + precioMax);
    }


    @Override
    public void eliminarProductoEspecifico(Integer idProductoEsp) {
        productoEspRepository.deleteById(idProductoEsp);
    }

}

