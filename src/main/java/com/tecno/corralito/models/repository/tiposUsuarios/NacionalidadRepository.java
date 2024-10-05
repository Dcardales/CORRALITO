package com.tecno.corralito.models.repository.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface NacionalidadRepository extends JpaRepository<Nacionalidad, Long> {
    Optional<Nacionalidad> findByDescripcion(String descripcion);
}

