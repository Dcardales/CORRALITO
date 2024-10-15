package com.tecno.corralito.services.productoEspecifico.impl;

import com.tecno.corralito.exceptions.CategoriaNotFoundException;
import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.exceptions.ZonaNotFoundException;
import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.repository.productoEspecifico.ProductoEspRepository;
import com.tecno.corralito.models.repository.productoGeneral.CategoriaRepository;
import com.tecno.corralito.models.repository.productoGeneral.ProductoRepository;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.ComercioRepository;
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
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ComercioRepository comercioRepository;

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
    public ProductoEsp crearProductoEspExistente(ProductoEspExistenteDto dto, Integer idComercio) {
        // Buscar el producto en el repositorio
        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

        // Buscar la zona
        Zona zona = zonaRepository.findById(dto.getIdZona())
                .orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada"));

        // Mapear el DTO a ProductoEsp
        ProductoEsp productoEsp = productoEspMapper.toEntity(dto, producto, zona);
        productoEsp.setComercio(comercioRepository.findById(idComercio).get());

        // Guardar el ProductoEsp
        return productoEspRepository.save(productoEsp);
    }

    @Override
    public ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto, Integer idComercio) {
        // Crear el producto personalizado
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

        // Buscar la zona
        Zona zona = zonaRepository.findById(dto.getIdZona())
                .orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada"));

        // Mapear el DTO a ProductoEsp
        ProductoEsp productoEsp = productoEspMapper.toEntityPersonalizado(dto, categoria, zona, comercioRepository.findById(idComercio).get());

        // Guardar el ProductoEsp
        return productoEspRepository.save(productoEsp);
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

