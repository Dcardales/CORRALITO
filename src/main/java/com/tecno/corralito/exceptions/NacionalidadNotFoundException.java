package com.tecno.corralito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NacionalidadNotFoundException extends RuntimeException {
  public NacionalidadNotFoundException(String message) {
    super(message);
  }
}