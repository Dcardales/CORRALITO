package com.tecno.corralito.models.repository.productoGeneral;


import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Verificar si una categoría ya existe por su nombre
    boolean existsByNombreCategoria(String nombreCategoria);

    // Verificar si otra categoría con el mismo nombre existe (diferente ID)
    boolean existsByNombreCategoriaAndIdCategoriaNot(String nombreCategoria, Integer idCategoria);

    // Buscar una categoría por su nombre
    Optional<Categoria> findByNombreCategoria(String nombreCategoria);
}

