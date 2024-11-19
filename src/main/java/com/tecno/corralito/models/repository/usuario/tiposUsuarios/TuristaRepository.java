package com.tecno.corralito.models.repository.usuario.tiposUsuarios;


import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TuristaRepository extends JpaRepository<Turista, Integer> {
    List<Turista> findByNacionalidad_IdNacionalidad(Long idNacionalidad);
    Optional<Turista> findByUsuario_Id(Long userId);
}