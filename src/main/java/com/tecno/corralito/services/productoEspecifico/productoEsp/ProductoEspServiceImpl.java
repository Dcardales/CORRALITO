package com.tecno.corralito.services.productoEspecifico.productoEsp;


import com.tecno.corralito.exceptions.CategoriaNotFoundException;
import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.exceptions.ZonaNotFoundException;
import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import com.tecno.corralito.models.repository.productoEspecifico.ProductoEspRepository;
import com.tecno.corralito.models.repository.productoGeneral.CategoriaRepository;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.ComercioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoEspServiceImpl implements IProductoEspService {

    @Autowired
    private ProductoEspRepository productoEspRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoEspMapper productoEspMapper;


    @Override
    public ProductoEsp obtenerProductoEspecifico(Integer idProductoEsp) {
        return productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto específico no encontrado"));
    }

    @Override
    public List<ProductoEsp> listarProductosEspecificos(Integer idComercio) {
        return productoEspRepository.findByComercioId(idComercio);
    }

    @Override
    @Transactional
    public void eliminarProductoEspecifico(Integer idProductoEsp) {
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
        productoEspRepository.delete(productoEsp);
    }

    @Transactional
    @Override
    public ProductoEsp crearProductoEspPersonalizado(ProductoEspPersonalizadoDto dto) {
        // Obtener el comercio del usuario autenticado directamente
        UserEntity usuario = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Comercio comercio = usuario.getComercio();

        // Validar zona y categoría
        Zona zona = zonaRepository.findById(dto.getIdZona())
                .orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada"));

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

        // Mapear el DTO a ProductoEsp
        ProductoEsp productoEsp = productoEspMapper.toProductoEsp(dto);
        productoEsp.setComercio(comercio);
        productoEsp.setZona(zona);
        productoEsp.setCategoria(categoria);

        return productoEspRepository.save(productoEsp);
    }

    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    @Override
    public ProductoEsp actualizarProductoEsp(Integer idProductoEsp, ProductoEspPersonalizadoDto dto) {
        // Buscar el producto específico existente
        ProductoEsp productoExistente = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto específico no encontrado"));

        // Obtener el comercio del usuario autenticado directamente
        UserEntity usuario = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Comercio comercio = usuario.getComercio();

        // Validar zona y categoría
        Zona zona = zonaRepository.findById(dto.getIdZona())
                .orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada"));

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada"));

        // Actualizar los datos del producto existente con los datos del DTO
        productoExistente.setNombreEspecifico(dto.getNombreProducto());
        productoExistente.setPrecio(dto.getPrecio());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setEstado(dto.getEstado());
        productoExistente.setZona(zona);
        productoExistente.setCategoria(categoria);
        productoExistente.setComercio(comercio); // Asignar automáticamente el comercio

        // Guardar el producto actualizado en la base de datos
        return productoEspRepository.save(productoExistente);
    }


}

