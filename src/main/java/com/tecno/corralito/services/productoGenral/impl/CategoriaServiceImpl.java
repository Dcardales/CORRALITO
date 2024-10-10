package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.exceptions.CategoriaYaExisteException;
import com.tecno.corralito.exceptions.CategoriaNotFoundException;
import com.tecno.corralito.mapper.CategoriaMapper;
import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.repository.productoGeneral.CategoriaRepository;
import com.tecno.corralito.services.productoGenral.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Override
    public CategoriaDto crearCategoria(CategoriaDto categoriaDto) {
        // Validación: Verificar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.existsByNombreCategoria(categoriaDto.getNombreCategoria())) {
            throw new CategoriaYaExisteException("La categoría con nombre '" + categoriaDto.getNombreCategoria() + "' ya existe.");
        }

        // Convertir el DTO a entidad, guardar y devolver como DTO
        Categoria categoria = categoriaMapper.toEntity(categoriaDto);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(savedCategoria);
    }

    @Override
    public CategoriaDto actualizarCategoria(Integer idCategoria, CategoriaDto categoriaDto) {
        // Validar si la categoría existe
        Categoria categoriaExistente = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con ID: " + idCategoria));

        // Validación: Verificar si ya existe otra categoría con el mismo nombre
        if (categoriaRepository.existsByNombreCategoriaAndIdCategoriaNot(categoriaDto.getNombreCategoria(), idCategoria)) {
            throw new CategoriaYaExisteException("Ya existe una categoría con el nombre '" + categoriaDto.getNombreCategoria() + "'.");
        }

        // Actualizar los campos
        categoriaExistente.setNombreCategoria(categoriaDto.getNombreCategoria());
        categoriaExistente.setDescripcionCategoria(categoriaDto.getDescripcionCategoria());

        // Guardar los cambios
        Categoria updatedCategoria = categoriaRepository.save(categoriaExistente);
        return categoriaMapper.toDto(updatedCategoria);
    }

    @Override
    public void eliminarCategoria(Integer idCategoria) {
        // Validar si la categoría existe
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con ID: " + idCategoria));

        // Eliminar la categoría
        categoriaRepository.delete(categoria);
    }

    @Override
    public List<CategoriaDto> listarCategorias() {
        // Listar todas las categorías
        List<Categoria> categorias = categoriaRepository.findAll();
        return categoriaMapper.toDtoList(categorias);
    }

    @Override
    public CategoriaDto obtenerCategoriaPorId(Integer idCategoria) {
        // Obtener una categoría por su ID
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con ID: " + idCategoria));
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaDto obtenerCategoriaPorNombre(String nombreCategoria) {
        // Buscar la categoría por su nombre
        Categoria categoria = categoriaRepository.findByNombreCategoria(nombreCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con nombre: " + nombreCategoria));
        return categoriaMapper.toDto(categoria);
    }
}


