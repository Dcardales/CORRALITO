package com.tecno.corralito.controllers.ProductoGeneral;



import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoGenral.IZonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ZonaController {

    @Autowired
    private IZonaService zonaService;

    @GetMapping("/zonas")
    public ResponseEntity<MensajeResponse> showAll() {
        List<Zona> zonaList = zonaService.listAll();
        if (zonaList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay registros")
                            .object(null)
                            .build(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(zonaList)
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping("/zonas")
    public ResponseEntity<MensajeResponse> create(@RequestBody ZonaDto zonaDto) {
        Zona zonaSave = null;
        try {
            zonaSave = zonaService.save(zonaDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(ZonaDto.builder()
                            .idZona(zonaSave.getIdZona())
                            .nombreZona(zonaSave.getNombreZona())
                            .build())
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PutMapping("/zonas/{id}")
    public ResponseEntity<MensajeResponse> update(@RequestBody ZonaDto zonaDto, @PathVariable Integer id) {
        Zona zonaUpdate = null;
        try {
            if (zonaService.existsById(id)) {
                zonaDto.setIdZona(id);
                zonaUpdate = zonaService.save(zonaDto);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(ZonaDto.builder()
                                .idZona(zonaUpdate.getIdZona())
                                .nombreZona(zonaUpdate.getNombreZona())
                                .build())
                        .build(),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("/zonas/{id}")
    public ResponseEntity<MensajeResponse> delete(@PathVariable Integer id) {
        try {
            Zona zonaDelete = zonaService.findById(id);
            zonaService.delete(zonaDelete);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("/zonas/{id}")
    public ResponseEntity<MensajeResponse> showById(@PathVariable Integer id) {
        Zona zona = zonaService.findById(id);

        if (zona == null) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar no existe.")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(ZonaDto.builder()
                                .idZona(zona.getIdZona())
                                .nombreZona(zona.getNombreZona())
                                .build())
                        .build(),
                HttpStatus.OK);
    }
}
