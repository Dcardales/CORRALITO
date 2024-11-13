package com.tecno.corralito.controllers.auth;


import com.tecno.corralito.models.dto.tiposUsuario.comercio.AuthCreateComercioRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.Auth.AuthLoginRequest;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.services.usuarios.auth.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.authService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/register-turista")
    public ResponseEntity<?> registerTurista(@Valid @RequestBody AuthCreateTuristaRequest turistaRequest, BindingResult result) {
        // Verifica si hay errores de validación
        if (result.hasErrors()) {
            // Mapea los errores de validación a un formato amigable
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        // Llama al servicio si no hay errores de validación
        AuthResponse response = authService.registerTurista(turistaRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-comercio")
    public ResponseEntity<AuthResponse> registerComercio(@Valid @RequestBody AuthCreateComercioRequest comercioRequest, BindingResult result) {
        // Verifica si hay errores de validación
        if (result.hasErrors()) {
            // Mapea los errores de validación a un formato amigable
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        // Llama al servicio si no hay errores de validación
        AuthResponse response = authService.registerComercio(comercioRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-enteRegulador")
    public ResponseEntity<AuthResponse> registerEnteRegulador(@Valid @RequestBody AuthCreateEnteRequest enteRequest, BindingResult result) {
        // Verifica si hay errores de validación
        if (result.hasErrors()) {
            // Mapea los errores de validación a un formato amigable
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        // Llama al servicio si no hay errores de validación
        AuthResponse response = authService.registerEnteRegulador(enteRequest);
        return ResponseEntity.ok(response);
    }


}
