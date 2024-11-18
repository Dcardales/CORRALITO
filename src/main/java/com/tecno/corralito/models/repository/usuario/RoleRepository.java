package com.tecno.corralito.models.repository.usuario;


import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
