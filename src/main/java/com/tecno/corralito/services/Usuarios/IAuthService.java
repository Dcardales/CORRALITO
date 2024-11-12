package com.tecno.corralito.services.Usuarios;

import com.tecno.corralito.models.dto.Auth.AuthCreateComercioRequest;
import com.tecno.corralito.models.dto.Auth.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.Auth.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.Auth.AuthLoginRequest;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {


    AuthResponse loginUser(AuthLoginRequest loginRequest);

    UserDetails loadUserByUsername(String email);

    AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest);

    AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest);

    AuthResponse registerEnteRegulador(AuthCreateEnteRequest enteRequest);

    AuthResponse registerAdministrador(CreateAdminRequest adminRequest);
}
