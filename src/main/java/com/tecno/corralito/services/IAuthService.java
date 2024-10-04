package com.tecno.corralito.services;

import com.tecno.corralito.models.dto.Auth.AuthCreateComercioRequest;
import com.tecno.corralito.models.dto.Auth.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.Auth.AuthLoginRequest;
import com.tecno.corralito.models.response.AuthResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {


    AuthResponse loginUser(AuthLoginRequest loginRequest);

    AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest);

    UserDetails loadUserByUsername(String email);

    @Transactional
    AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest);
}
