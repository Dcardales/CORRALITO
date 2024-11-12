package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.models.response.ProductoEspResponse;
import com.tecno.corralito.services.productoEspecifico.IProductoEspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos-especificos")
public class ProductoEspController {

    @Autowired
    private IProductoEspService productoEspService;

    @Autowired
    private ProductoEspMapper productoEspMapper;


    // Obtener un producto específico por su ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponse> obtenerProductoEspecifico(@PathVariable Integer id) {
        ProductoEsp productoEsp = productoEspService.obtenerProductoEspecifico(id);
        ProductoEspResponse response = productoEspMapper.toProductoEspResponse(productoEsp);

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico encontrado")
                .object(response)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }

    // Listar productos específicos por ID de comercio
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/comercio/{idComercio}")
    public ResponseEntity<MensajeResponse> listarProductosEspecificosPorComercio(@PathVariable Integer idComercio) {
        List<ProductoEsp> productos = productoEspService.listarProductosEspecificos(idComercio);
        List<ProductoEspResponse> productoResponses = productos.stream()
                .map(productoEspMapper::toProductoEspResponse)
                .collect(Collectors.toList());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos específicos obtenida exitosamente")
                .object(productoResponses)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }

    // Crear un producto específico
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearProductoEspPersonalizado(
            @RequestBody ProductoEspPersonalizadoDto dto) {

        // Llamamos al servicio para crear el producto específico
        ProductoEsp nuevoProducto = productoEspService.crearProductoEspPersonalizado(dto);

        // Convertir la entidad ProductoEsp a ProductoEspResponse usando el mapper
        ProductoEspResponse productoEspResponse = productoEspMapper.toProductoEspResponse(nuevoProducto);

        // Crear la respuesta con un mensaje
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico creado exitosamente")
                .object(productoEspResponse)
                .build();

        // Retornar la respuesta con status 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeResponse);
    }


    // Actualizar un producto específico
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PutMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> actualizarProductoEsp(
            @PathVariable Integer idProductoEsp,
            @RequestBody ProductoEspPersonalizadoDto dto) {

        // Llamamos al servicio para actualizar el producto específico
        ProductoEsp productoActualizado = productoEspService.actualizarProductoEsp(idProductoEsp, dto);

        // Convertir la entidad ProductoEsp a ProductoEspResponse usando el mapper
        ProductoEspResponse productoEspResponse = productoEspMapper.toProductoEspResponse(productoActualizado);

        // Crear la respuesta con un mensaje
        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico actualizado exitosamente")
                .object(productoEspResponse)
                .build();

        // Retornar la respuesta con status 200 (OK)
        return new ResponseEntity<>(mensajeResponse, HttpStatus.OK);
    }


    // Eliminar un producto específico por su ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminarProductoEspecifico(@PathVariable Integer id) {
        productoEspService.eliminarProductoEspecifico(id);

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico eliminado exitosamente")
                .object(null)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


}

