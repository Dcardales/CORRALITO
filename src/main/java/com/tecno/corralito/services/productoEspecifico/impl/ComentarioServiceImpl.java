package com.tecno.corralito.services.productoEspecifico.impl;

import com.tecno.corralito.exceptions.*;
import com.tecno.corralito.mapper.ComentarioMapper;
import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import com.tecno.corralito.models.repository.productoEspecifico.ComentarioRepository;
import com.tecno.corralito.models.repository.productoEspecifico.ProductoEspRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.services.productoEspecifico.IComentarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Override
    public ComentarioDto agregarComentario(Integer idProductoEsp, ComentarioDto comentarioDto) {
        // Verificar si el producto específico existe
        ProductoEsp productoEsp = productoEspRepository.findById(idProductoEsp)
                .orElseThrow(() -> new ProductoNotFoundException("Producto Especifico no encontrado"));

        // Obtener el usuario autenticado
        UserEntity usuario = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Obtener el turista logueado
        Turista turista = usuario.getTurista();

        // Verificar si el usuario ya ha comentado este producto
        if (comentarioRepository.existsByProductoEspAndTurista(productoEsp, turista)) {
            throw new ComentarioExistenteException("El usuario ya ha comentado este producto.");
        }

        // Mapear el DTO a la entidad Comentario
        Comentario comentario = comentarioMapper.toEntity(comentarioDto);

        // Asociar el turista al comentario
        comentario.setTurista(turista);
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

    @Transactional
    @Override
    public ComentarioDto actualizarComentario(Integer idComentario, ComentarioDto comentarioDto) {
        // Verificar si el comentario existe
        Comentario comentarioExistente = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado"));

        // Verificar si el usuario autenticado es el autor del comentario
        UserEntity usuario = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (!comentarioExistente.getTurista().equals(usuario.getTurista())) {
            throw new UnauthorizedAccessException("No puedes actualizar un comentario que no te pertenece.");
        }

        // Actualizar los datos del comentario
        comentarioExistente.setDescripcion(comentarioDto.getDescripcion());
        comentarioExistente.setValoracion(comentarioDto.getValoracion());

        // Guardar el comentario actualizado
        Comentario comentarioActualizado = comentarioRepository.save(comentarioExistente);

        // Mapear el comentario actualizado a DTO y devolverlo
        return comentarioMapper.toDto(comentarioActualizado);
    }


    @Override
    public void eliminarComentario(Integer idComentario) {
        // Verificar si el comentario existe
        Comentario comentarioExistente = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentario no encontrado"));

        // Verificar si el usuario autenticado es el autor del comentario
        UserEntity usuario = userRepository.findByEmail(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (!comentarioExistente.getTurista().equals(usuario.getTurista())) {
            throw new UnauthorizedAccessException("No puedes eliminar un comentario que no te pertenece.");
        }

        // Eliminar el comentario
        comentarioRepository.delete(comentarioExistente);
    }

    private String getCurrentUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}


