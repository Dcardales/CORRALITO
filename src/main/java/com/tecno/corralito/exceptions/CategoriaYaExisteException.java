package com.tecno.corralito.exceptions;


public class CategoriaYaExisteException extends RuntimeException {
    public CategoriaYaExisteException(String message) {
        super(message);
    }
}

