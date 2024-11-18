package com.tecno.corralito.models.repository.usuario.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnteReguladorRepository extends JpaRepository<EnteRegulador, Integer> {
    Optional<EnteRegulador> findByIdentificacion(String identificacion);
}