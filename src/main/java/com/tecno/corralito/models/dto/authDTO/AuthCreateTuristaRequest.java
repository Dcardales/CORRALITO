package com.tecno.corralito.models.dto.authDTO;

import com.tecno.corralito.models.entities.enums.Genero;

public record AuthCreateTuristaRequest(
        String nombre,
        String apellidos,
        String correo,
        String password,
        Genero genero,
        String telefono,
        NacionalidadRequest nacionalidad
) {
}