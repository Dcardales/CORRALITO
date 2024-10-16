package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspExistenteDto;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
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
    @PostMapping("/existente/{idComercio}")
    public ResponseEntity<MensajeResponse> crearProductoEspExistente(
            @PathVariable Integer idComercio,
            @RequestBody ProductoEspExistenteDto dto) {

        ProductoEsp productoEsp = productoEspService.crearProductoEspExistente(dto, idComercio);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico creado exitosamente a partir de un producto existente")
                .object(productoEsp)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PostMapping("/personalizado/{idComercio}")
    public ResponseEntity<MensajeResponse> crearProductoEspPersonalizado(
            @PathVariable Integer idComercio,
            @RequestBody ProductoEspPersonalizadoDto dto) {

        ProductoEsp productoEsp = productoEspService.crearProductoEspPersonalizado(dto, idComercio);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico personalizado creado exitosamente")
                .object(productoEsp)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> obtenerProductoEspecifico(@PathVariable Integer idProductoEsp) {

        ProductoEsp productoEsp = productoEspService.obtenerProductoEspecifico(idProductoEsp);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico obtenido exitosamente")
                .object(productoEsp)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/comercio/{idComercio}")
    public ResponseEntity<MensajeResponse> listarProductosEspecificos(@PathVariable Integer idComercio) {

        List<ProductoEsp> productosEspecificos = productoEspService.listarProductosEspecificos(idComercio);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Lista de productos específicos obtenida exitosamente")
                .object(productosEspecificos)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @DeleteMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> eliminarProductoEspecifico(@PathVariable Integer idProductoEsp) {

        productoEspService.eliminarProductoEspecifico(idProductoEsp);

        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Producto específico eliminado exitosamente")
                .object(null)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

