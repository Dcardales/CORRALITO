package com.tecno.ctgbank.models.repository;


import com.tecno.ctgbank.models.entity.usuario.RoleEntity;
import com.tecno.ctgbank.models.entity.enums.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);

    Optional<RoleEntity> findByRoleEnum(RoleEnum roleEnum);
}