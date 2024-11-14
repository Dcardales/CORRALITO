package com.tecno.corralito.controllers.usuarios;

import com.tecno.corralito.models.dto.Auth.PasswordResetRequest;
import com.tecno.corralito.models.dto.tiposUsuario.comercio.AuthCreateComercioRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.models.response.usuarios.comercio.ComercioResponse;
import com.tecno.corralito.models.response.usuarios.comercio.UpdateComercioRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import com.tecno.corralito.services.usuarios.comercio.IComercioService;
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
@RequestMapping("/api/comercio")
public class ComercioController {
    ;

    @Autowired
    IComercioService comercioService;

    // Método para registrar un comercio
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerComercio(@Valid @RequestBody AuthCreateComercioRequest comercioRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        AuthResponse response = comercioService.registerComercio(comercioRequest);
        return ResponseEntity.ok(response);
    }

    // Método para actualizar un comercio
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ComercioResponse> actualizarComercio(@PathVariable Integer id, @Valid @RequestBody UpdateComercioRequest comercioRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new ComercioResponse("Errores de validación: " + String.join(", ", errors), null));
        }

        ComercioResponse response = comercioService.actualizarComercio(id, comercioRequest);
        return ResponseEntity.ok(response);
    }

    // Método para eliminar un comercio
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminarComercio(@PathVariable Integer id) {
        try {
            comercioService.eliminarComercio(id);
            return ResponseEntity.ok("Comercio eliminado exitosamente");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // Método para listar todos los comercios
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<Comercio>> listarComercios() {
        List<Comercio> comercios = comercioService.listarComercios();
        return ResponseEntity.ok(comercios);
    }

    // Método para obtener un comercio por ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @GetMapping("/{id}")
    public ResponseEntity<Comercio> obtenerComercio(@PathVariable Integer id) {
        try {
            Comercio comercio = comercioService.obtenerComercioPorId(id);
            return ResponseEntity.ok(comercio);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Método para restablecer la contraseña
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMERCIO')")
    @PostMapping("/reset-password/{comercioId}")
    public ResponseEntity<String> restablecerContraseña(@PathVariable Integer comercioId, @RequestBody PasswordResetRequest request) {
        try {
            comercioService.restablecerContraseña(comercioId, request.getNewPassword());
            return ResponseEntity.ok("Contraseña restablecida exitosamente");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}

