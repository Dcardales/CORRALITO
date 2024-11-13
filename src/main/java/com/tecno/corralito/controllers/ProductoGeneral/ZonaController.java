package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.response.general.MensajeResponse;
import com.tecno.corralito.services.productoGenral.zona.IZonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/zonas")
public class ZonaController {

    @Autowired
    private IZonaService iZonaService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PostMapping
    public ResponseEntity<MensajeResponse> crearZona(@RequestBody ZonaDto zonaDto) {
        ZonaDto nuevaZona = iZonaService.crearZona(zonaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona creada exitosamente")
                .object(nuevaZona)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> actualizarZona(
            @PathVariable Integer id,
            @RequestBody ZonaDto zonaDto) {
        ZonaDto zonaActualizada = iZonaService.actualizarZona(id, zonaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona actualizada exitosamente")
                .object(zonaActualizada)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> eliminarZona(@PathVariable Integer id) {
        iZonaService.eliminarZona(id);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona eliminada exitosamente")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @GetMapping
    public ResponseEntity<MensajeResponse> listarTodasLasZonas() {
        List<ZonaDto> listaZonas = iZonaService.listarTodas();
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Lista de todas las zonas")
                .object(listaZonas)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTEREGULADOR')")
    @GetMapping("/buscar")
    public ResponseEntity<MensajeResponse> buscarPorNombre(@RequestParam String nombreZona) {
        ZonaDto zona = iZonaService.buscarPorNombre(nombreZona);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona encontrada")
                .object(zona)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}