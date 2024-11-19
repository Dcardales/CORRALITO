package com.tecno.corralito.models.repository.usuario.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    Optional<Comercio> findByNit(String nit);
    Optional<Comercio> findByNombreComercio(String nombreComercio);
    Optional<Comercio> findByUsuario_Id(Long userId);
}
