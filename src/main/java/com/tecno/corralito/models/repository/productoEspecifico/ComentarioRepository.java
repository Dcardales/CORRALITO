package com.tecno.corralito.models.repository.productoEspecifico;

import com.tecno.corralito.models.entity.productoEspecifico.Comentario;
import com.tecno.corralito.models.entity.productoEspecifico.ProductoEsp;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    List<Comentario> findByProductoEsp_IdProductoEsp(Integer idProductoEsp);

    boolean existsByProductoEspAndTurista(ProductoEsp productoEsp, Turista turista);
}
