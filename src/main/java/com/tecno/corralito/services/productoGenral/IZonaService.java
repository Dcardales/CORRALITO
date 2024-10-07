package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IZonaService {

    List<Zona> listAll();

    Zona save(@Valid ZonaDto zonaDto);

    Zona findById(Integer id);

    void delete(Zona zona);

    boolean existsById(Integer id);

    Optional<Zona> findByNombreZona(String nombreZona);
}




