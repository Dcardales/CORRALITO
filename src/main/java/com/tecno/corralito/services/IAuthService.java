package com.tecno.corralito.services;

import com.tecno.corralito.models.dto.usuarios.AuthCreateTuristaRequest;
import com.tecno.corralito.models.request.Auth.AuthCreateUserRequest;
import com.tecno.corralito.models.request.Auth.AuthLoginRequest;
import com.tecno.corralito.models.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {


    AuthResponse createUser(AuthCreateUserRequest createUserRequest);

    AuthResponse loginUser(AuthLoginRequest loginRequest);

    AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest);

    UserDetails loadUserByUsername(String email);
}
