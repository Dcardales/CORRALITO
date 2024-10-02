package com.tecno.corralito.models.repositories.usuario;

import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NacionalidadRepository extends JpaRepository<Nacionalidad, Long> {
    Optional<Nacionalidad> findByDescripcion(String descripcion);
}
