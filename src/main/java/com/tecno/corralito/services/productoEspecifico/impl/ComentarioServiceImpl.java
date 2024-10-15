package com.tecno.corralito.services.productoEspecifico.impl;

import com.tecno.corralito.exceptions.ProductoNotFoundException;
import com.tecno.corralito.mapper.ComentarioMapper;
import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.repository.productoEspecifico.ComentarioRepository;
import com.tecno.corralito.models.repository.productoEspecifico.ProductoEspRepository;
import com.tecno.corralito.services.productoEspecifico.IComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioServiceImpl implements IComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ProductoEspRepository productoEspRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Override
    public ComentarioDto agregarComentario(Integer idProductoEsp, ComentarioDto comentarioDto) {
        // Verificar si el producto específico existe
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto Especifico no encontrado"));

        // Crear y guardar el comentario
        Comentario comentario = comentarioMapper.toEntity(comentarioDto);
        comentario.setProductoEsp(productoEsp);

        // Guardar el comentario en la base de datos
        Comentario comentarioGuardado = comentarioRepository.save(comentario);

        // Mapear el comentario guardado a DTO y devolverlo
        return comentarioMapper.toDto(comentarioGuardado);
    }

    @Override
    public List<ComentarioDto> obtenerComentariosPorProductoEspecifico(Integer idProductoEsp) {
        // Verificar si el producto específico existe
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto Especifico no encontrado"));

        // Obtener comentarios del producto específico y mapearlos a DTOs con el nombre del turista
        List<Comentario> comentarios = comentarioRepository.findByProductoEsp_IdProductoEsp(idProductoEsp);
        return comentarios.stream()
                .map(comentarioMapper::toDtoWithTurista)
                .collect(Collectors.toList());
    }
}


