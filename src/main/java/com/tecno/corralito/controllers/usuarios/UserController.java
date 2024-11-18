package com.tecno.corralito.controllers.usuarios;

import com.tecno.corralito.models.dto.tiposUsuario.usuario.UserDto;
import com.tecno.corralito.services.usuarios.usuario.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/corralito/v1/usuarios")
public class UserController {

    @Autowired
    private IUserService userService;


    @GetMapping
    public ResponseEntity<List<UserDto>> listAllUsers() {
        List<UserDto> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }


    @GetMapping("/profiles")
    public ResponseEntity<List<String>> getUserProfiles() {
        List<String> profiles = userService.getUserProfiles();
        return ResponseEntity.ok(profiles);
    }
}
