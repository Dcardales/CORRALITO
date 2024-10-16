package com.tecno.corralito.services.productoEspecifico.impl;

import com.tecno.corralito.exceptions.CategoriaNotFoundException;
import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.exceptions.ZonaNotFoundException;
import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
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
    public ProductoEsp obtenerProductoEspecifico(Integer idProductoEsp) {
        return productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ResourceNotFoundException("Producto específico no encontrado con el ID: " + idProductoEsp));
    }

    @Override
    public List<ProductoEsp> listarProductosEspecificos(Integer idComercio) {
        Comercio comercio = comercioRepository.findById(idComercio)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio no encontrado con el ID: " + idComercio));

        return productoEspRepository.findByComercioId(idComercio);
    }

    // Listar productos por zona
    public List<ProductoEsp> listarProductosPorZona(Integer idZona) {
        Zona zona = zonaRepository.findById(idZona)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con el ID: " + idZona));

        return productoEspRepository.findByZona(zona);
    }

    @Override
    public void eliminarProductoEspecifico(Integer idProductoEsp) {
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ResourceNotFoundException("Producto específico no encontrado con el ID: " + idProductoEsp));

        productoEspRepository.delete(productoEsp);
    }

}

