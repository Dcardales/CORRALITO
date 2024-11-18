package com.tecno.corralito.models.repository.usuario.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TuristaRepository extends JpaRepository<Turista, Integer> {
    List<Turista> findByNacionalidad_IdNacionalidad(Long idNacionalidad);
}