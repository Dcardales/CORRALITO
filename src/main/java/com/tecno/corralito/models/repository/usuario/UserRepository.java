package com.tecno.corralito.models.repository.usuario;


import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Encuentra un usuario por su email
    Optional<UserEntity> findByEmail(String email);

    // Encuentra usuarios que tengan un rol específico
    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r.roleEnum = :role")
    List<UserEntity> findUsersByRole(@Param("role") RoleEnum role);

    // Verifica si un usuario existe por email
    boolean existsByEmail(String email);
}

