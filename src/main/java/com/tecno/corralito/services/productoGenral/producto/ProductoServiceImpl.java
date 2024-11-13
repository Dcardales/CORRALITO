package com.tecno.corralito.services.productoGenral.producto;


import com.tecno.corralito.exceptions.PrecioInvalidoException;
import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.exceptions.ProductoYaExisteException;
import com.tecno.corralito.mapper.ProductoMapper;
import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.repository.productoGeneral.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Override
    public ProductoDto crearProducto(ProductoDto productoDto) {

        // Validar que no exista un producto con el mismo nombre en la misma zona
        if (productoRepository.existsByNombreProductoAndZonaIdZona(productoDto.getNombreProducto(), productoDto.getIdZona())) {
            throw new ProductoYaExisteException("El nombre del producto ya existe en la misma zona");
        }

        // Validar que el precio máximo sea mayor que el precio mínimo
        if (productoDto.getPrecioMax().compareTo(productoDto.getPrecioMin()) <= 0) {
            throw new PrecioInvalidoException("El precio máximo debe ser mayor que el precio mínimo");
        }

        // Convertir DTO a entidad y guardar en la base de datos
        Producto producto = productoMapper.toEntity(productoDto);
        Producto productoGuardado = productoRepository.save(producto);

        // Convertir entidad guardada de vuelta a DTO y retornar
        return productoMapper.toDto(productoGuardado);
    }

    @Override
    public ProductoDto actualizarProducto(Integer id, ProductoDto productoDto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));

        // Validar que no exista un producto con el mismo nombre en la misma zona
        if (productoRepository.existsByNombreProductoAndZonaIdZona(productoDto.getNombreProducto(), productoDto.getIdZona())) {
            throw new ProductoYaExisteException("El nombre del producto ya existe en la misma zona");
        }

        // Validar que el precio máximo sea mayor que el precio mínimo
        if (productoDto.getPrecioMax().compareTo(productoDto.getPrecioMin()) <= 0) {
            throw new PrecioInvalidoException("El precio máximo debe ser mayor que el precio mínimo");
        }

        // Actualizar los campos del producto existente usando el mapper
        Producto productoActualizado = productoMapper.toEntity(productoDto);
        productoActualizado.setIdProducto(id);

        Producto productoGuardado = productoRepository.save(productoActualizado);
        return productoMapper.toDto(productoGuardado);
    }

    @Override
    public void eliminarProducto(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }

    @Override
    public List<ProductoDto> listarProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productoMapper.toDtoList(productos);
    }

    @Override
    public ProductoDto buscarProductoPorNombre(String nombreProducto) {
        Producto producto = productoRepository.findByNombreProducto(nombreProducto)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con nombre: " + nombreProducto));
        return productoMapper.toDto(producto);
    }
}

