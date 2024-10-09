package com.tecno.corralito.models.repository.productoGeneral;

import com.tecno.corralito.models.entity.productoGeneral.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByNombreProducto(String nombreProducto);

    boolean existsByNombreProductoAndZonaIdZona(String nombreProducto, Integer idZona);
}

