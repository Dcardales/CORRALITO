package com.tecno.corralito.services.usuarios.turista;


import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.repository.usuario.NacionalidadRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class NacionalidadImplService implements INacionalidadService {

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @Override
    @PostConstruct
    public void cargarNacionalidades() {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            String codigo = iso;
            String descripcion = locale.getDisplayCountry();


            if (nacionalidadRepository.findByDescripcion(descripcion).isEmpty()) {
                Nacionalidad nacionalidad = new Nacionalidad(codigo, descripcion);
                nacionalidadRepository.save(nacionalidad);
            }
        }
    }

    @Override
    public Optional<Nacionalidad> findByDescripcion(String descripcion) {
        return nacionalidadRepository.findByDescripcion(descripcion);
    }

}
