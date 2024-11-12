package com.tecno.corralito.services.Usuarios;

import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.UpdateAdminRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Administrador;
import com.tecno.corralito.models.response.AuthResponse;


import java.util.List;

public interface IAdminService {

    AuthResponse registerAdministrador(CreateAdminRequest adminRequest);


    Administrador updateAdministrador(Integer id, UpdateAdminRequest adminRequest);

    void deleteAdministrador(Integer userId);

    List<Administrador> getAllAdministradores();

    Administrador getAdministradorById(Integer id);
}
