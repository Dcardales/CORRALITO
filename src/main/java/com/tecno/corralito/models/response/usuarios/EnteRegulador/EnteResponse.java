package com.tecno.corralito.models.response.usuarios.EnteRegulador;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnteResponse {

    private String mensaje;
    private EnteRegulador enteRegulador;

    public EnteResponse(EnteRegulador enteRegulador, String mensaje) {
        this.enteRegulador = enteRegulador;
        this.mensaje = mensaje;
    }
}


