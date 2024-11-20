package com.tecno.corralito.models.repository.productoEspecifico;

import com.tecno.corralito.models.entity.productoEspecifico.Divisa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DivisaRepository extends JpaRepository<Divisa, Long> {
    Optional<Divisa> findByCodigo(String codigo);
}
