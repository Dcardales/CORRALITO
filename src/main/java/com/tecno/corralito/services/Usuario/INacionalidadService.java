package com.tecno.corralito.services.Usuario;


import com.tecno.corralito.models.entity.usuario.Nacionalidad;

import java.util.Optional;

public interface INacionalidadService {
    void cargarNacionalidades();

    Optional<Nacionalidad> findByDescripcion(String descripcion);
}
