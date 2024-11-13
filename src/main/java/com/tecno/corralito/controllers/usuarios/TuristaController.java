package com.tecno.corralito.controllers.usuarios;


import com.tecno.corralito.models.dto.Auth.PasswordResetRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.TuristaUpdateRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.services.usuarios.turista.ITuristaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/turistas")
public class TuristaController {



    @Autowired
   private ITuristaService turistaService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerTurista(@Valid @RequestBody AuthCreateTuristaRequest turistaRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        AuthResponse response = turistaService.registerTurista(turistaRequest);
        return ResponseEntity.ok(response);
    }

    // Actualización de Turista
    @PutMapping("/{id}")
    public ResponseEntity<Turista> updateTurista(@PathVariable Integer id, @Valid @RequestBody TuristaUpdateRequest turistaUpdateRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(null);
        }

        Turista updatedTurista = turistaService.updateTurista(id, turistaUpdateRequest);
        return ResponseEntity.ok(updatedTurista);
    }

    // Eliminación de Turista
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTurista(@PathVariable Integer id) {
        turistaService.deleteTurista(id);
        return ResponseEntity.ok("Turista eliminado exitosamente");
    }


    @PostMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Integer id, @RequestBody @Valid PasswordResetRequest passwordResetRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body("Errores de validación: " + String.join(", ", errors));
        }

        turistaService.resetPassword(id, passwordResetRequest.getNewPassword());
        return ResponseEntity.ok("Contraseña restablecida exitosamente");
    }
}

