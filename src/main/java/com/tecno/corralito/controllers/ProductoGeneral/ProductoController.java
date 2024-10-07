package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoGenral.IProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<MensajeResponse> listAll() {
        List<ProductoDto> productos = productoService.listAll();
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Lista de productos obtenida con éxito")
                .object(productos)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponse> getById(@PathVariable("id") Integer id) {
        ProductoDto producto = productoService.findById(id);
        if (producto != null) {
            MensajeResponse response = MensajeResponse.builder()
                    .mensaje("Producto encontrado")
                    .object(producto)
                    .build();
            return ResponseEntity.ok(response);
        } else {
            MensajeResponse response = MensajeResponse.builder()
                    .mensaje("Producto no encontrado")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<MensajeResponse> create(@Valid @RequestBody ProductoDto productoDto, BindingResult result) {
        if (result.hasErrors()) {
            MensajeResponse errorResponse = MensajeResponse.builder()
                    .mensaje("Errores de validación")
                    .object(result.getAllErrors())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        ProductoDto createdProducto = productoService.save(productoDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto creado con éxito")
                .object(createdProducto)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> update(@PathVariable("id") Integer id,
                                                  @Valid @RequestBody ProductoDto productoDto,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            MensajeResponse errorResponse = MensajeResponse.builder()
                    .mensaje("Errores de validación")
                    .object(result.getAllErrors())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        ProductoDto updatedProducto = productoService.save(productoDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto actualizado con éxito")
                .object(updatedProducto)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> delete(@PathVariable("id") Integer id) {
        if (productoService.existsById(id)) {
            productoService.delete(id);
            MensajeResponse response = MensajeResponse.builder()
                    .mensaje("Producto eliminado con éxito")
                    .build();
            return ResponseEntity.noContent().build();
        } else {
            MensajeResponse response = MensajeResponse.builder()
                    .mensaje("Producto no encontrado para eliminar")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

