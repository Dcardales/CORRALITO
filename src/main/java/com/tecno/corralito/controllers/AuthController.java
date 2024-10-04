package com.tecno.corralito.controllers;


import com.tecno.corralito.models.dto.usuarios.AuthCreateTuristaRequest;
import com.tecno.corralito.services.IAuthService;
import com.tecno.corralito.models.request.Auth.AuthCreateUserRequest;
import com.tecno.corralito.models.request.Auth.AuthLoginRequest;
import com.tecno.corralito.models.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.authService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.authService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register-turista")
    public ResponseEntity<AuthResponse> registerTurista(@RequestBody @Valid AuthCreateTuristaRequest turistaRequest) {
        return new ResponseEntity<>(this.authService.registerTurista(turistaRequest), HttpStatus.CREATED);
    }
}
