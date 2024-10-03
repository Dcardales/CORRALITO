package com.tecno.corralito.services.impl;

import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import com.tecno.corralito.models.repositories.usuario.NacionalidadRepository;
import com.tecno.corralito.services.INacionalidadService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NacionalidadServiceImpl implements INacionalidadService {

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @PostConstruct
    @Override
    public void cargarNacionalidades() {
        List<String> paisesCargados = nacionalidadRepository.findAll()
                .stream()
                .map(Nacionalidad::getDescripcion)
                .collect(Collectors.toList());

        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            String descripcion = locale.getDisplayCountry();

            if (!paisesCargados.contains(descripcion)) {
                Nacionalidad nacionalidad = new Nacionalidad(iso, descripcion);
                nacionalidadRepository.save(nacionalidad);
            }
        }
    }

    @Override
    public Nacionalidad getNacionalidadByDescripcion(String descripcion) {
        return nacionalidadRepository.findByDescripcion(descripcion)
                .orElseThrow(() -> new IllegalArgumentException("Nacionalidad no encontrada."));
    }

}
