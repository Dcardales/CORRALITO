package com.tecno.corralito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsuarioNotFoundException extends RuntimeException {
  public UsuarioNotFoundException(String message) {
    super(message);
  }
}
