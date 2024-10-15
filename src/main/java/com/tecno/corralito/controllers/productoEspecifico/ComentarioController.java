package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.ComentarioDto;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoEspecifico.IComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private IComentarioService comentarioService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('TURISTA')")
    @PostMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> agregarComentario(
            @PathVariable Integer idProductoEsp,
            @RequestBody ComentarioDto comentarioDto) {

        ComentarioDto comentarioCreado = comentarioService.agregarComentario(idProductoEsp, comentarioDto);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Comentario agregado exitosamente")
                .object(comentarioCreado)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('TURISTA')")
    @GetMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> obtenerComentariosPorProductoEspecifico(
            @PathVariable Integer idProductoEsp) {

        List<ComentarioDto> comentarios = comentarioService.obtenerComentariosPorProductoEspecifico(idProductoEsp);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Comentarios obtenidos exitosamente")
                .object(comentarios)
                .build();

        return ResponseEntity.ok(response);
    }
}

