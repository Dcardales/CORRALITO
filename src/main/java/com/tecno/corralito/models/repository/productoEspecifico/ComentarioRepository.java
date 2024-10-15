package com.tecno.corralito.models.repository.productoEspecifico;

import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    List<Comentario> findByProductoEsp_IdProductoEsp(Integer idProductoEsp);
}
