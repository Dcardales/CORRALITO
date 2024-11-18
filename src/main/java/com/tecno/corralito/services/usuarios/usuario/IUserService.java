package com.tecno.corralito.services.usuarios.usuario;

import com.tecno.corralito.models.dto.tiposUsuario.usuario.UserDto;

import java.util.List;

public interface IUserService {
    List<UserDto> listAllUsers();
    void deleteUser(Long userId);
    List<String> getUserProfiles();
}
