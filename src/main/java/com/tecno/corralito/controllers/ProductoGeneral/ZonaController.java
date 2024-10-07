package com.tecno.corralito.controllers.ProductoGeneral;


import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoGenral.IZonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/zonas")
@Validated
public class ZonaController {

    private final IZonaService zonaService;

    @Autowired
    public ZonaController(IZonaService zonaService) {
        this.zonaService = zonaService;
    }

    @GetMapping
    public ResponseEntity<List<Zona>> getAllZonas() {
        List<Zona> zonas = zonaService.listAll();
        return ResponseEntity.ok(zonas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponse> getZonaById(@PathVariable Integer id) {
        Zona zona = zonaService.findById(id);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona encontrada con éxito")
                .object(zona)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MensajeResponse> createZona(@Valid @RequestBody ZonaDto zonaDto) {
        Zona nuevaZona = zonaService.save(zonaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona creada con éxito")
                .object(nuevaZona)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MensajeResponse> updateZona(@PathVariable Integer id, @Valid @RequestBody ZonaDto zonaDto) {
        zonaDto.setIdZona(id); // Aseguramos que el ID sea el correcto
        Zona zonaActualizada = zonaService.save(zonaDto);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona actualizada con éxito")
                .object(zonaActualizada)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeResponse> deleteZona(@PathVariable Integer id) {
        Zona zona = zonaService.findById(id);  // Verificamos si la zona existe
        zonaService.delete(zona);
        MensajeResponse response = MensajeResponse.builder()
                .mensaje("Zona eliminada con éxito")
                .object(zona)
                .build();
        return ResponseEntity.ok(response);
    }
}