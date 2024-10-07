package com.tecno.corralito.services.Usuario;

import com.tecno.corralito.models.dto.Auth.*;
import com.tecno.corralito.models.response.AuthResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {


    AuthResponse loginUser(AuthLoginRequest loginRequest);

    UserDetails loadUserByUsername(String email);

    AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest);

    AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest);

    AuthResponse registerEnteRegulador(AuthCreateEnteRequest enteRequest);

    AuthResponse registerAdministrador(AuthCreateAdminRequest adminRequest);
}
