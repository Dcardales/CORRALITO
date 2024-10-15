package com.tecno.corralito.services.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;

import java.util.List;

public interface IComentarioService {

    List<ComentarioDto> obtenerComentariosPorProductoEspecifico(Integer idProductoEsp);

    ComentarioDto agregarComentario(Integer idProductoEsp, ComentarioDto comentarioDto);
}