package com.tecno.corralito.models.repository.productoGeneral;

import com.tecno.corralito.models.entity.productoGeneral.Zona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ZonaRepository extends JpaRepository<Zona, Integer> {
    Optional<Zona> findByNombreZona(String nombreZona);

    boolean existsByNombreZona(String nombreZona);

}
