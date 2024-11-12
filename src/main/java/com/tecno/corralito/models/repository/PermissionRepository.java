package com.tecno.corralito.models.repository;

import com.tecno.corralito.models.entity.usuario.PermissionEntity;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<PermissionEntity, Long> {
}
