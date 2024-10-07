package com.tecno.corralito.models.repository.productoGeneral;

import com.tecno.corralito.models.entity.productoGeneral.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Puedes agregar métodos personalizados si es necesario, por ejemplo:
    List<Producto> findByNombreProductoContaining(String nombre);
}
