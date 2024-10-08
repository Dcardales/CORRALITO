package com.tecno.corralito.models.repository.productoGeneral;

import com.tecno.corralito.models.entity.productoGeneral.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByNombreProducto(String nombreProducto);

    boolean existsByNombreProductoAndIdProductoNot(String nombreProducto, Integer idProducto);

    Optional<Producto> findByNombreProducto(String nombreProducto);
}
