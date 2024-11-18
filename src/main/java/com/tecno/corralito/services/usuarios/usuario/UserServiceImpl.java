package com.tecno.corralito.services.usuarios.usuario;

import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.models.dto.tiposUsuario.usuario.UserDto;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.AdministradorRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.ComercioRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.EnteReguladorRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.TuristaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private TuristaRepository turistaRepository;

    @Autowired
    private EnteReguladorRepository enteReguladorRepository;

    @Override
    public List<UserDto> listAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());

            // Extrae los nombres de los roles y los mapea a Strings
            Set<String> perfiles = user.getRoles().stream()
                    .map(role -> role.getRoleEnum().name()) // Convierte RoleEnum a String
                    .collect(Collectors.toSet());
            dto.setPerfiles(perfiles);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Limpia las relaciones de roles manualmente si es necesario (opcional)
        user.getRoles().clear();

        // Elimina las relaciones específicas (Administrador, Comercio, etc.)
        if (user.getAdministrador() != null) {
            administradorRepository.delete(user.getAdministrador());
        }
        if (user.getComercio() != null) {
            comercioRepository.delete(user.getComercio());
        }
        if (user.getTurista() != null) {
            turistaRepository.delete(user.getTurista());
        }
        if (user.getEnteRegulador() != null) {
            enteReguladorRepository.delete(user.getEnteRegulador());
        }

        // Finalmente elimina el usuario
        userRepository.delete(user);
    }

    @Override
    public List<String> getUserProfiles() {
        return Arrays.asList("Administrador", "Comercio", "Turista", "Ente Regulador");
    }
}
