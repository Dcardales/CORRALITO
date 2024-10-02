package com.tecno.corralito.models.repositories.tiposUsuarios;

import com.tecno.corralito.models.entities.tiposUsuarios.Turista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TuristaRepository extends JpaRepository<Turista,Long> {
}
