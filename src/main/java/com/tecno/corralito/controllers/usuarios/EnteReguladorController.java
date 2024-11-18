package com.tecno.corralito.controllers.usuarios;

import com.tecno.corralito.models.dto.Auth.PasswordResetRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.UpdateEnteRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.services.usuarios.enteRegulador.IEnteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/corralito/v1/ente-regulador")
public class EnteReguladorController {

    @Autowired
    private IEnteService enteService;

    // Registro de un nuevo Ente Regulador
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerEnteRegulador(@Valid @RequestBody AuthCreateEnteRequest enteRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        AuthResponse response = enteService.registerEnteRegulador(enteRequest);
        return ResponseEntity.ok(response);
    }

    // Actualizar los datos del Ente Regulador
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEnteRegulador(@PathVariable Integer id, @Valid @RequestBody UpdateEnteRequest enteRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body("Errores de validación: " + String.join(", ", errors));
        }

        try {
            enteService.updateEnteRegulador(id, enteRequest);
            return ResponseEntity.ok("Ente Regulador actualizado exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ente Regulador no encontrado");
        }
    }

    // Obtener información de un Ente Regulador por ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<EnteRegulador> getEnteRegulador(@PathVariable Integer id) {
        try {
            EnteRegulador enteRegulador = enteService.getEnteReguladorById(id);
            return ResponseEntity.ok(enteRegulador);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar un Ente Regulador por ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEnteRegulador(@PathVariable Integer id) {
        try {
            enteService.deleteEnteRegulador(id);
            return ResponseEntity.ok("Ente Regulador eliminado exitosamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ente Regulador no encontrado");
        }
    }

    // Restablecer la contraseña del Ente Regulador
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset-password/{enteID}")
    public ResponseEntity<String> restablecerContraseña(@PathVariable Integer enteId, @RequestBody PasswordResetRequest request) {
        try {
            enteService.restablecerContraseña(enteId, request.getNewPassword());
            return ResponseEntity.ok("Contraseña restablecida exitosamente");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
