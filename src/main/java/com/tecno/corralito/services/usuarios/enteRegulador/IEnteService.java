package com.tecno.corralito.services.usuarios.enteRegulador;

import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.UpdateEnteRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.models.response.usuarios.EnteRegulador.EnteResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IEnteService
{

    AuthResponse registerEnteRegulador(AuthCreateEnteRequest enteRequest);

    EnteResponse updateEnteRegulador(Integer id, UpdateEnteRequest enteRequest);

    void deleteEnteRegulador(Integer id);

    List<EnteRegulador> listEnteReguladores();

    EnteRegulador getEnteReguladorById(Integer id);

    void restablecerContraseña(Integer id, String newPassword);
}
