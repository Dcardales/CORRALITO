package com.tecno.corralito.services;

import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import jakarta.annotation.PostConstruct;

public interface INacionalidadService {
    @PostConstruct
    void cargarNacionalidades();

    Nacionalidad getNacionalidadByDescripcion(String descripcion);
}
