package com.tecno.corralito.controllers.usuarios;


import com.tecno.corralito.models.dto.tiposUsuario.administrador.AdministradorDto;
import com.tecno.corralito.models.dto.tiposUsuario.comercio.UpdateComercio;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.EnteReguladorDto;
import com.tecno.corralito.models.dto.tiposUsuario.turista.ObtenerTurista;
import com.tecno.corralito.models.dto.tiposUsuario.usuario.UserDto;
import com.tecno.corralito.services.usuarios.administrador.IAdminService;
import com.tecno.corralito.services.usuarios.comercio.IComercioService;
import com.tecno.corralito.services.usuarios.enteRegulador.IEnteService;
import com.tecno.corralito.services.usuarios.turista.ITuristaService;
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

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IEnteService enteService;

    @Autowired
    private IComercioService comercioService;

    @Autowired
    private ITuristaService turistaService;



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

    @GetMapping("/{userId}/{type}")
    public ResponseEntity<?> getUserByType(@PathVariable Long userId, @PathVariable String type) {
        switch (type.toLowerCase()) {
            case "turista":
                ObtenerTurista obtenerTurista = turistaService.getTuristaByUserId(userId);
                return ResponseEntity.ok(obtenerTurista);
            case "administrador":
                AdministradorDto administradorDto = adminService.getAdministradorByUserId(userId);
                return ResponseEntity.ok(administradorDto);
            case "comercio":
                UpdateComercio updateComercio = comercioService.getComercioByUserId(userId);
                return ResponseEntity.ok(updateComercio);
            case "enteregulador":
                EnteReguladorDto enteReguladorDto = enteService.getEnteReguladorByUserId(userId);
                return ResponseEntity.ok(enteReguladorDto);
            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + type);
        }
    }
}

