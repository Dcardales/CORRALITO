package com.tecno.corralito.models.repositories.usuario;


import com.tecno.corralito.models.entities.usuario.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByCorreo(String coreo);


}
