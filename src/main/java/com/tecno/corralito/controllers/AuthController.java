package com.tecno.corralito.controllers;

import com.tecno.corralito.models.dto.authDTO.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.authDTO.AuthLoginRequest;
import com.tecno.corralito.models.dto.authDTO.AuthResponse;
import com.tecno.corralito.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    // Endpoint para registrar un turista
    @PostMapping("/register/turista")
    public ResponseEntity<AuthResponse> registerTurista(@RequestBody AuthCreateTuristaRequest authCreateTuristaRequest) {
        AuthResponse response = authService.registerTurista(authCreateTuristaRequest);
        return ResponseEntity.ok(response);
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthLoginRequest authLoginRequest) {
        AuthResponse response = authService.loginUser(authLoginRequest);
        return ResponseEntity.ok(response);
    }
}
