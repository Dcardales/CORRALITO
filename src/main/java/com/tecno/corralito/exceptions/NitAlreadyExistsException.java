package com.tecno.corralito.exceptions;

public class NitAlreadyExistsException extends RuntimeException {
    public NitAlreadyExistsException(String message) {
        super(message);
    }
}