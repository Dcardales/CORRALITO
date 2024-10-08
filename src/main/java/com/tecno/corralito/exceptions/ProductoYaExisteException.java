package com.tecno.corralito.exceptions;

public class ProductoYaExisteException extends RuntimeException {
    public ProductoYaExisteException(String message) {
        super(message);
    }
}
