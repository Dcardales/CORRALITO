package com.tecno.corralito.services;


import com.tecno.corralito.models.entity.usuario.Nacionalidad;

import java.util.Optional;

public interface INacionalidadService {
    void cargarNacionalidades();

    Optional<Nacionalidad> findByDescripcion(String descripcion);
}
