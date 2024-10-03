package com.tecno.corralito.models.repositories.tiposUsuarios;

import com.tecno.corralito.models.entities.tiposUsuarios.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
}
