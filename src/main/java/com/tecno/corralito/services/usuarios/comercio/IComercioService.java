package com.tecno.corralito.services.usuarios.comercio;

import com.tecno.corralito.models.dto.tiposUsuario.comercio.AuthCreateComercioRequest;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.models.response.usuarios.comercio.ComercioResponse;
import com.tecno.corralito.models.response.usuarios.comercio.UpdateComercioRequest;

import java.util.List;

public interface IComercioService {


    AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest);

    ComercioResponse actualizarComercio(Integer id, UpdateComercioRequest comercioRequest);

    void eliminarComercio(Integer id);

    List<Comercio> listarComercios();

    Comercio obtenerComercioPorId(Integer id);

    void restablecerContraseña(Integer comercioId, String nuevaContraseña);
}
