package com.tecno.corralito.models.repository.productoEspecifico;

import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoEspRepository extends JpaRepository<ProductoEsp, Integer> {

    List<ProductoEsp> findByEstado(Estado estado);

    List<ProductoEsp> findByComercioId(Integer comercioId);

    List<ProductoEsp> findByZona(Zona zona);

}
