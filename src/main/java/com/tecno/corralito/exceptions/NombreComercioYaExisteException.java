package com.tecno.corralito.exceptions;

public class NombreComercioYaExisteException extends RuntimeException {
    public NombreComercioYaExisteException(String mensaje) {
        super(mensaje);
    }
}
