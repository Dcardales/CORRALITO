package com.tecno.corralito.models.response.usuarios.comercio;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;

public class ComercioResponse {
    private String mensaje;
    private Comercio comercio;

    public ComercioResponse(String mensaje, Comercio comercio) {
        this.mensaje = mensaje;
        this.comercio = comercio;
    }

}