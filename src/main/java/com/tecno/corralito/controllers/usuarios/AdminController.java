package com.tecno.corralito.controllers.usuarios;


import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.UpdateAdminRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Administrador;
import com.tecno.corralito.models.response.AuthResponse;
import com.tecno.corralito.services.Usuarios.IAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/administradores")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody CreateAdminRequest adminRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Errores de validación: " + String.join(", ", errors), null, false));
        }

        AuthResponse response = adminService.registerAdministrador(adminRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Administrador> updateAdmin(@PathVariable Integer id, @Valid @RequestBody UpdateAdminRequest adminRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errores de validación: " + String.join(", ", errors));
        }

        Administrador updatedAdmin = adminService.updateAdministrador(id, adminRequest);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdministrador(@PathVariable Integer id) {
        adminService.deleteAdministrador(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }



    @GetMapping("/all")
    public ResponseEntity<List<Administrador>> getAllAdmins() {
        List<Administrador> administradores = adminService.getAllAdministradores();
        return ResponseEntity.ok(administradores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getAdminById(@PathVariable Integer id) {
        Administrador administrador = adminService.getAdministradorById(id);
        return ResponseEntity.ok(administrador);
    }
}

