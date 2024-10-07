package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;

import java.util.List;

public interface IZonaService {

    List<Zona> listAll();

    Zona save(ZonaDto perfil);

    Zona findById(Integer id);

    void delete(Zona perfil);

    boolean existsById(Integer id);

    public default ZonaDto convertirZonaAZonaDto(Zona zona) {
        ZonaDto zonaDto = new ZonaDto();
        zonaDto.setIdZona(zona.getIdZona());
        zonaDto.setNombreZona(zona.getNombreZona());
        return zonaDto;
    }
}




