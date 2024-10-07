package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.services.productoGenral.IZonaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ZonaImplService implements IZonaService {


    private final ZonaRepository zonaRepository;

    @Autowired
    public ZonaImplService( ZonaRepository zonaRepository) {
        this.zonaRepository = zonaRepository;
    }

    @Override
    public List<Zona> listAll() {
        return StreamSupport.stream(zonaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Zona save(ZonaDto zonaDto) {

        if (zonaDto.getIdZona() != null && existsById(zonaDto.getIdZona())) {
            Zona zonaExistente = findById(zonaDto.getIdZona());
            zonaExistente.setNombreZona(zonaDto.getNombreZona());
            return zonaRepository.save(zonaExistente);
        } else {
            Zona zonaNueva = Zona.builder()
                    .nombreZona(zonaDto.getNombreZona())
                    .build();
            return zonaRepository.save(zonaNueva);
        }

    }

    @Override
    public Zona findById(Integer id) {
        return zonaRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Zona zona) {
        zonaRepository.delete(zona);
    }

    @Override
    public boolean existsById(Integer id) {
        return zonaRepository.existsById(id);
    }
}
