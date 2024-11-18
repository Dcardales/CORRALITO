package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.ProductoDto;
import com.tecno.corralito.models.response.general.MensajeResponse;
import com.tecno.corralito.services.productoGenral.producto.IProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corralito/v1/productos")
public class ProductoController {

    @Autowired
    private IProductoService iProductoService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearProducto(@Valid @RequestBody ProductoDto productoDto) {
        ProductoDto productoCreado = iProductoService.crearProducto(productoDto);
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto creado exitosamente")
                .object(productoCreado)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> actualizarProducto(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoDto productoDto) {

        ProductoDto productoActualizado = iProductoService.actualizarProducto(id, productoDto);
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto actualizado exitosamente")
                .object(productoActualizado)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminarProducto(@PathVariable Integer id) {
        iProductoService.eliminarProducto(id);
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto eliminado exitosamente")
                .object(null)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }

    @GetMapping
    public ResponseEntity<MensajeResponse> listarProductos() {
        List<ProductoDto> productos = iProductoService.listarProductos();
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos obtenida exitosamente")
                .object(productos)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }

    @GetMapping("/buscar")
    public ResponseEntity<MensajeResponse> buscarProductoPorNombre(@RequestParam String nombre) {
        ProductoDto producto = iProductoService.buscarProductoPorNombre(nombre);
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto encontrado")
                .object(producto)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }
}


