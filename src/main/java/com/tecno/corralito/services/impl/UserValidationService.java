package com.tecno.corralito.services.impl;

import com.tecno.corralito.models.exception.UserAlreadyExistsException;
import com.tecno.corralito.models.repositories.usuario.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    @Autowired
    private UserRepository userRepository;

    public void validateUniqueCorreo(String correo) {
        if (userRepository.findByCorreo(correo).isPresent()) {
            throw new UserAlreadyExistsException("El correo ya está registrado. Por favor, elija otro.");
        }
    }
}
