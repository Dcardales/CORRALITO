package com.tecno.corralito.services.productoGenral;

import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;

import java.util.List;


public interface IZonaService {

    ZonaDto crearZona(ZonaDto zonaDto);

    ZonaDto actualizarZona(Integer id, ZonaDto zonaDto);

    void eliminarZona(Integer id);

    List<ZonaDto> listarTodas();

    ZonaDto buscarPorNombre(String nombreZona);
}




