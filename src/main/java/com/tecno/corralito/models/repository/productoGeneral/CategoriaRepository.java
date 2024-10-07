package com.tecno.corralito.models.repository.productoGeneral;


import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByNombreCategoria(String nombreCategoria);
}
