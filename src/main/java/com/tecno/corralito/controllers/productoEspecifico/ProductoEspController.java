package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.CreateProductoEsp;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoEspecifico.IProductoEspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producto-especifico")
public class ProductoEspController {

    @Autowired
    private IProductoEspService productoEspService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> obtenerProductoEspecifico(@PathVariable Integer idProductoEsp) {
        CreateProductoEsp createProductoEsp = productoEspService.obtenerProductoEspecifico(idProductoEsp);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico encontrado")
                .object(createProductoEsp)
                .build();
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping
    public ResponseEntity<MensajeResponse> listarProductosEspecificos() {
        List<CreateProductoEsp> productosEsp = productoEspService.listarProductosEspecificos();
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Lista de productos específicos")
                .object(productosEsp)
                .build();
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearProductoEspecifico(@RequestBody CreateProductoEsp createProductoEsp) {
        CreateProductoEsp nuevoProductoEsp = productoEspService.crearProductoEspecifico(createProductoEsp);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico creado exitosamente")
                .object(nuevoProductoEsp)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @DeleteMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> eliminarProductoEspecifico(@PathVariable Integer idProductoEsp) {
        productoEspService.eliminarProductoEspecifico(idProductoEsp);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico eliminado exitosamente")
                .object(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}