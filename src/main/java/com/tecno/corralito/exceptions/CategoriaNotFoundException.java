package com.tecno.corralito.exceptions;

public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(Integer id) {
        super("Categoría con ID " + id + " no encontrada.");
    }
}
