package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.exceptions.DatabaseOperationException;
import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.mapper.ProductoMapper;
import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.repository.productoGeneral.ProductoRepository;
import com.tecno.corralito.services.productoGenral.IProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoImplService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper; // Mapper de MapStruct


    @Override
    public List<ProductoDto> listAll() {
        List<Producto> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            throw new ProductoNotFoundException("No se encontraron productos.");
        }
        return productoMapper.toDtoList(productos);
    }

    @Transactional
    @Override
    public ProductoDto save(@Valid ProductoDto productoDto) {
        if (productoDto.getPrecioMin().compareTo(productoDto.getPrecioMax()) > 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el precio máximo.");
        }

        Producto producto = productoMapper.toEntity(productoDto); // Usar MapStruct para la conversión
        try {
            Producto savedProducto = productoRepository.save(producto);
            return productoMapper.toDto(savedProducto);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error al guardar el producto.", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoDto findById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
        return productoMapper.toDto(producto);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado.");
        }
        try {
            productoRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new DatabaseOperationException("Error al eliminar el producto.", ex);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        return productoRepository.existsById(id);
    }
}

