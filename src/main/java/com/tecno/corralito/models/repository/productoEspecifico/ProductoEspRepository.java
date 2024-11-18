package com.tecno.corralito.models.repository.productoEspecifico;

import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoEspRepository extends JpaRepository<ProductoEsp, Integer> {

    List<ProductoEsp> findByEstado(Estado estado);

    List<ProductoEsp> findByComercioId(Integer comercioId);

    List<ProductoEsp> findByZona_IdZona(Integer idZona);

    Page<ProductoEsp> findByCategoria_IdCategoria(Integer idCategoria, Pageable pageable);

    Page<ProductoEsp> findByCategoria_IdCategoriaAndZona_IdZona(Integer idCategoria, Integer idZona, Pageable pageable);

    List<ProductoEsp> findAllByOrderByIdProductoEspDesc();

    List<ProductoEsp> findAllByOrderByPrecioDesc();

    List<ProductoEsp> findAllByOrderByPrecioAsc();

    Page<ProductoEsp> findAll(Pageable pageable);


}
