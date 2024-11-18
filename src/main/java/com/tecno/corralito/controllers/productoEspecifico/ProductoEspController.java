package com.tecno.corralito.controllers.productoEspecifico;

import com.tecno.corralito.mapper.ProductoEspMapper;
import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspPersonalizadoDto;
import com.tecno.corralito.models.dto.productoEspecifico.productoEsp.ProductoEspSimple;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.response.general.MensajeResponse;
import com.tecno.corralito.models.response.productoEspesifico.productoEsp.ProductoEspResponse;
import com.tecno.corralito.services.productoEspecifico.productoEsp.IProductoEspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/corralito/v1/productos-especificos")
public class ProductoEspController {

    @Autowired
    private IProductoEspService productoEspService;

    @Autowired
    private ProductoEspMapper productoEspMapper;


    // Obtener un producto específico por su ID
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


    @GetMapping("/comercio/{idComercio}")
    public ResponseEntity<MensajeResponse> listarProductosEspecificosPorComercio(@PathVariable Integer idComercio) {
        List<ProductoEsp> productos = productoEspService.listarProductosEspecificosComercio(idComercio);
        List<ProductoEspSimple> productoSimples = productos.stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos específicos obtenida exitosamente")
                .object(productoSimples)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<MensajeResponse> listarProductosPorCategoriaPaginado(
            @PathVariable Integer idCategoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoEsp> productosPage = productoEspService.listarProductosPorCategoria(idCategoria, pageable);

        List<ProductoEspSimple> productoSimples = productosPage.getContent().stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("productos", productoSimples);
        response.put("currentPage", productosPage.getNumber());
        response.put("totalItems", productosPage.getTotalElements());
        response.put("totalPages", productosPage.getTotalPages());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista paginada de productos obtenida exitosamente")
                .object(response)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


    @GetMapping("/productos")
    public ResponseEntity<MensajeResponse> listarTodosLosProductosEspecificos(
            @RequestParam(defaultValue = "0") int page, // Página actual
            @RequestParam(defaultValue = "10") int size // Tamaño de la página
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoEsp> productosPage = productoEspService.listarTodosLosProductosEspecificos(pageable);

        List<ProductoEspSimple> productoSimples = productosPage.getContent().stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("productos", productoSimples);
        response.put("currentPage", productosPage.getNumber());
        response.put("totalItems", productosPage.getTotalElements());
        response.put("totalPages", productosPage.getTotalPages());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista paginada de todos los productos específicos obtenida exitosamente")
                .object(response)
                .build();

        return ResponseEntity.ok(mensajeResponse);

    }
    @GetMapping("/categoria/{idCategoria}/zona/{idZona}")
    public ResponseEntity<MensajeResponse> listarProductosPorCategoriaYZona(
            @PathVariable Integer idCategoria,
            @PathVariable Integer idZona,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoEsp> productosPage = productoEspService.listarProductosPorCategoriaYZona(idCategoria, idZona, pageable);

        List<ProductoEspSimple> productoSimples = productosPage.getContent().stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("productos", productoSimples);
        response.put("currentPage", productosPage.getNumber());
        response.put("totalItems", productosPage.getTotalElements());
        response.put("totalPages", productosPage.getTotalPages());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista paginada de productos específicos por categoría y zona obtenida exitosamente")
                .object(response)
                .build();

        return ResponseEntity.ok(mensajeResponse);
    }

    @GetMapping("/zona/{idZona}")
    public ResponseEntity<MensajeResponse> listarProductosPorZona(@PathVariable Integer idZona) {
        List<ProductoEspSimple> productoSimples = productoEspService.listarProductosPorZonaSimple(idZona);

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos por zona obtenida exitosamente")
                .object(productoSimples)
                .build();

        return ResponseEntity.ok(mensajeResponse);
    }

    @GetMapping("/recientes")
    public ResponseEntity<MensajeResponse> listarProductosMasRecientes() {
        List<ProductoEsp> productos = productoEspService.listarProductosMasRecientes();
        List<ProductoEspSimple> productoSimples = productos.stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos más recientes obtenida exitosamente")
                .object(productoSimples)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


    @GetMapping("/precio/mayor")
    public ResponseEntity<MensajeResponse> listarProductosPorMayorPrecio() {
        List<ProductoEsp> productos = productoEspService.listarProductosPorMayorPrecio();
        List<ProductoEspSimple> productoSimples = productos.stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos por mayor precio obtenida exitosamente")
                .object(productoSimples)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


    @GetMapping("/precio/menor")
    public ResponseEntity<MensajeResponse> listarProductosPorMenorPrecio() {
        List<ProductoEsp> productos = productoEspService.listarProductosPorMenorPrecio();
        List<ProductoEspSimple> productoSimples = productos.stream()
                .map(productoEspMapper::toProductoEspSimple)
                .collect(Collectors.toList());

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Lista de productos por menor precio obtenida exitosamente")
                .object(productoSimples)
                .build();
        return ResponseEntity.ok(mensajeResponse);
    }


    // Crear un producto específico
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearProductoEspPersonalizado(
            @RequestBody ProductoEspPersonalizadoDto dto) {
        ProductoEsp nuevoProducto = productoEspService.crearProductoEspPersonalizado(dto);
        ProductoEspResponse productoEspResponse = productoEspMapper.toProductoEspResponse(nuevoProducto);

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico creado exitosamente")
                .object(productoEspResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mensajeResponse);
    }



    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PutMapping("/{idProductoEsp}")
    public ResponseEntity<MensajeResponse> actualizarProductoEsp(
            @PathVariable Integer idProductoEsp,
            @RequestBody ProductoEspPersonalizadoDto dto) {
        ProductoEsp productoActualizado = productoEspService.actualizarProductoEsp(idProductoEsp, dto);
        ProductoEspResponse productoEspResponse = productoEspMapper.toProductoEspResponse(productoActualizado);

        MensajeResponse mensajeResponse = MensajeResponse.builder()
                .mensaje("Producto específico actualizado exitosamente")
                .object(productoEspResponse)
                .build();

        return new ResponseEntity<>(mensajeResponse, HttpStatus.OK);
    }


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

