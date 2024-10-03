package com.tecno.corralito.models.repositories.usuario;


import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}
