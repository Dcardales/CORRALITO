package com.tecno.corralito.controllers.ProductoGeneral;



import com.tecno.corralito.models.dto.productoGeneral.CategoriaDto;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.response.MensajeResponse;
import com.tecno.corralito.services.productoGenral.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/categorias")
    public ResponseEntity<MensajeResponse> showAll() {
        List<Categoria> categoriaList = categoriaService.listAll();
        if (categoriaList.isEmpty()) {
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
                        .object(categoriaList)
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping("/categorias")
    public ResponseEntity<MensajeResponse> create(@RequestBody CategoriaDto categoriaDto) {
        Categoria categoriaSave = null;
        try {
            categoriaSave = categoriaService.save(categoriaDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(CategoriaDto.builder()
                            .idCategoia(categoriaSave.getIdCategoia())
                            .nombreCategoria(categoriaSave.getNombreCategoria())
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

    @PutMapping("/categorias/{id}")
    public ResponseEntity<MensajeResponse> update(@RequestBody CategoriaDto categoriaDto, @PathVariable Integer id) {
        Categoria categoriaUpdate = null;
        try {
            if (categoriaService.existsById(id)) {
                categoriaDto.setIdCategoia(id);
                categoriaUpdate = categoriaService.save(categoriaDto);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(CategoriaDto.builder()
                                .idCategoia(categoriaUpdate.getIdCategoia())
                                .nombreCategoria(categoriaUpdate.getNombreCategoria())
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

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<MensajeResponse> delete(@PathVariable Integer id) {
        try {
            Categoria categoriaDelete = categoriaService.findById(id);
            categoriaService.delete(categoriaDelete);
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

    @GetMapping("/categorias/{id}")
    public ResponseEntity<MensajeResponse> showById(@PathVariable Integer id) {
        Categoria categoria = categoriaService.findById(id);

        if (categoria == null) {
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
                        .object(CategoriaDto.builder()
                                .idCategoia(categoria.getIdCategoia())
                                .nombreCategoria(categoria.getNombreCategoria())
                                .build())
                        .build(),
                HttpStatus.OK);
    }
}
