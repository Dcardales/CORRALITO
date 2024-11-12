package com.tecno.corralito.exceptions;

public class ComentarioNotFoundException extends RuntimeException {
    public ComentarioNotFoundException(String message) {
        super(message);
    }
}