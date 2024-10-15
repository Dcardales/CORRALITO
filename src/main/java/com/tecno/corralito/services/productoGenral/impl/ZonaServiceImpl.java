package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.exceptions.ZonaNotFoundException;
import com.tecno.corralito.exceptions.ZonaYaExisteException;
import com.tecno.corralito.mapper.ZonaMapper;
import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.services.productoGenral.IZonaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class ZonaServiceImpl implements IZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ZonaMapper zonaMapper;

    @Override
    public ZonaDto crearZona(ZonaDto zonaDto) {
        if (zonaRepository.existsByNombreZona(zonaDto.getNombreZona())) {
            throw new ZonaYaExisteException("La zona con nombre " + zonaDto.getNombreZona() + " ya existe.");
        }

        Zona zona = zonaMapper.toEntity(zonaDto);
        Zona savedZona = zonaRepository.save(zona);
        return zonaMapper.toDto(savedZona);
    }

    @Override
    public ZonaDto actualizarZona(Integer id, ZonaDto zonaDto) {
        Zona zonaExistente = zonaRepository.findById(id)
                .orElseThrow(() -> new ZonaNotFoundException("Zona con id " + id + " no encontrada."));

        // Validar si el nombre de la zona ya existe y pertenece a otra zona
        if (zonaRepository.existsByNombreZona(zonaDto.getNombreZona()) &&
                !zonaExistente.getNombreZona().equals(zonaDto.getNombreZona())) {
            throw new ZonaYaExisteException("La zona con nombre " + zonaDto.getNombreZona() + " ya existe.");
        }

        zonaExistente.setNombreZona(zonaDto.getNombreZona());
        zonaExistente.setDescripcionZona(zonaDto.getDescripcionZona());

        Zona updatedZona = zonaRepository.save(zonaExistente);
        return zonaMapper.toDto(updatedZona);
    }

    @Override
    public void eliminarZona(Integer id) {
        Zona zonaExistente = zonaRepository.findById(id)
                .orElseThrow(() -> new ZonaNotFoundException("Zona con id " + id + " no encontrada."));
        zonaRepository.delete(zonaExistente);
    }

    @Override
    public List<ZonaDto> listarTodas() {
        List<Zona> zonas = zonaRepository.findAll();
        return zonas.stream().map(zonaMapper::toDto).toList();
    }

    @Override
    public ZonaDto buscarPorNombre(String nombreZona) {
        Zona zona = zonaRepository.findByNombreZona(nombreZona)
                .orElseThrow(() -> new ZonaNotFoundException("Zona con nombre " + nombreZona + " no encontrada."));
        return zonaMapper.toDto(zona);
    }
}

