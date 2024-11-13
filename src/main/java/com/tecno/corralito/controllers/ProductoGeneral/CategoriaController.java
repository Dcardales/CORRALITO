package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.response.general.MensajeResponse;
import com.tecno.corralito.services.productoGenral.categoria.ICategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService iCategoriaService;


    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearCategoria(@RequestBody @Valid CategoriaDto categoriaDto) {
        CategoriaDto nuevaCategoria = iCategoriaService.crearCategoria(categoriaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Categoría creada exitosamente.")
                .object(nuevaCategoria)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> actualizarCategoria(@PathVariable Integer id,
                                                               @RequestBody @Valid CategoriaDto categoriaDto) {
        CategoriaDto categoriaActualizada = iCategoriaService.actualizarCategoria(id, categoriaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Categoría actualizada exitosamente.")
                .object(categoriaActualizada)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminarCategoria(@PathVariable Integer id) {
        iCategoriaService.eliminarCategoria(id);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Categoría eliminada exitosamente.")
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @GetMapping
    public ResponseEntity<MensajeResponse> listarCategorias() {
        List<CategoriaDto> categorias = iCategoriaService.listarCategorias();
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Lista de categorías obtenida exitosamente.")
                .object(categorias)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponse> obtenerCategoriaPorId(@PathVariable Integer id) {
        CategoriaDto categoria = iCategoriaService.obtenerCategoriaPorId(id);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Categoría obtenida exitosamente.")
                .object(categoria)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @GetMapping("/nombre/{nombreCategoria}")
    public ResponseEntity<MensajeResponse> obtenerCategoriaPorNombre(@PathVariable String nombreCategoria) {
        CategoriaDto categoriaDto = iCategoriaService.obtenerCategoriaPorNombre(nombreCategoria);
        MensajeResponse respuesta = MensajeResponse.builder()
                .mensaje("Categoría encontrada")
                .object(categoriaDto)
                .build();

        return ResponseEntity.ok(respuesta);
    }
}
