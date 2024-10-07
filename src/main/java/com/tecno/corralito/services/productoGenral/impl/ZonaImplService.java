package com.tecno.corralito.services.productoGenral.impl;


import com.tecno.corralito.exceptions.ZonaAlreadyExistsException;
import com.tecno.corralito.exceptions.ZonaNotFoundException;
import com.tecno.corralito.mapper.ZonaMapper;
import com.tecno.corralito.models.dto.productoGeneral.ZonaDto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.services.productoGenral.IZonaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;



@Service
public class ZonaImplService implements IZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    @Autowired
    private ZonaMapper zonaMapper;

    @Override
    public List<Zona> listAll() {
        return StreamSupport.stream(zonaRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Zona save(@Valid ZonaDto zonaDto) {
        // Verificar si el nombre de la zona ya existe
        if (zonaDto.getIdZona() == null) {
            Optional<Zona> existingZona = zonaRepository.findByNombreZona(zonaDto.getNombreZona());
            if (existingZona.isPresent()) {
                throw new ZonaAlreadyExistsException("Ya existe una zona con el nombre: " + zonaDto.getNombreZona());
            }

            // Crear nueva Zona
            Zona zonaNueva = zonaMapper.toEntity(zonaDto);
            return zonaRepository.save(zonaNueva);
        } else {
            throw new IllegalArgumentException("La zona ya existe");
        }
    }

    @Transactional
    public Zona update(@Valid ZonaDto zonaDto) {
        if (zonaDto.getIdZona() == null || !existsById(zonaDto.getIdZona())) {
            throw new ZonaNotFoundException("Zona no encontrada con id: " + zonaDto.getIdZona());
        }

        // Verificar si el nombre ya existe en otra zona
        Optional<Zona> zonaConMismoNombre = zonaRepository.findByNombreZona(zonaDto.getNombreZona());
        if (zonaConMismoNombre.isPresent() && !zonaConMismoNombre.get().getIdZona().equals(zonaDto.getIdZona())) {
            throw new ZonaAlreadyExistsException("Ya existe una zona con el nombre: " + zonaDto.getNombreZona());
        }

        // Actualizar Zona existente
        Zona zonaExistente = findById(zonaDto.getIdZona());
        zonaExistente.setNombreZona(zonaDto.getNombreZona());
        return zonaRepository.save(zonaExistente);
    }

    @Override
    public Zona findById(Integer id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada con id: " + id));
    }

    @Override
    public void delete(Zona zona) {
        if (!existsById(zona.getIdZona())) {
            throw new ZonaNotFoundException("No se puede eliminar. Zona no encontrada con id: " + zona.getIdZona());
        }
        zonaRepository.delete(zona);
    }

    @Override
    public boolean existsById(Integer id) {
        return zonaRepository.existsById(id);
    }

  @Override
  public Optional<Zona> findByNombreZona(String nombreZona) {
        return zonaRepository.findByNombreZona(nombreZona);
    }
}

