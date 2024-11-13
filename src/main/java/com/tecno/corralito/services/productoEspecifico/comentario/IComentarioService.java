package com.tecno.corralito.services.productoEspecifico.comentario;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;

import java.util.List;

public interface IComentarioService {

    List<ComentarioDto> obtenerComentariosPorProductoEspecifico(Integer idProductoEsp);

    ComentarioDto agregarComentario(Integer idProductoEsp, ComentarioDto comentarioDto);

    ComentarioDto actualizarComentario(Integer idComentario, ComentarioDto comentarioDto);

    void eliminarComentario(Integer idComentario);
}