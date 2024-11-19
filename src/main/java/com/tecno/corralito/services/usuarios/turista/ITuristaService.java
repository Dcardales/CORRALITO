package com.tecno.corralito.services.usuarios.turista;


import com.tecno.corralito.models.dto.tiposUsuario.turista.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.ObtenerTurista;
import com.tecno.corralito.models.dto.tiposUsuario.turista.TuristaUpdateRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import com.tecno.corralito.models.response.auth.AuthResponse;


public interface ITuristaService {

    AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest);

    ObtenerTurista getTuristaByUserId(Long userId);

    Turista updateTurista(Integer turistaId, TuristaUpdateRequest turistaUpdateRequest);

    void deleteTurista(Integer turistaId);

    void resetPassword(Integer turistaId, String newPassword);
}
