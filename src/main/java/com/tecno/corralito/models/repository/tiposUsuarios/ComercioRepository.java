package com.tecno.corralito.models.repository.tiposUsuarios;


import com.tecno.corralito.models.entity.tiposUsuarios.Comercio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComercioRepository extends JpaRepository<Comercio, Integer> {
    Optional<Comercio> findByNit(String nit);
    Optional<Comercio> findByNombreComercio(String nombreComercio);
}
