package com.tecno.corralito.exceptions;

public class ComentarioExistenteException extends RuntimeException {
    public ComentarioExistenteException(String message) {
        super(message);
    }
}