package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.services.productoGenral.impl.CategoriaImplService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaImplService categoriaService;


    // Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listarCategorias() {
        List<CategoriaDto> categorias = categoriaService.listAll();
        return ResponseEntity.ok(categorias);
    }

    // Obtener una categoría por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerCategoriaPorId(@PathVariable("id") Integer id) {
        CategoriaDto categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoria);
    }

    // Crear o actualizar una categoría
    @PostMapping
    public ResponseEntity<CategoriaDto> guardarCategoria(@Valid @RequestBody CategoriaDto categoriaDto) {
        CategoriaDto categoriaGuardada = categoriaService.save(categoriaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    // Eliminar una categoría por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable("id") Integer id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
