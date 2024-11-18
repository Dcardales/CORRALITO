package com.tecno.corralito.models.repository.usuario.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByIdentificacion(String identificacion);

}