package com.tecno.corralito.services;

import com.tecno.corralito.models.dto.authDTO.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.authDTO.AuthResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {
    UserDetails loadUserByCorreo(String correo);
    Authentication authenticate(String correo, String password);
    AuthResponse registerTurista(AuthCreateTuristaRequest authCreateTuristaRequest);
}