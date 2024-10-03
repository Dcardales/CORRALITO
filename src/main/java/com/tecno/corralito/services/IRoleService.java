package com.tecno.corralito.services;

import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.RoleEntity;


public interface IRoleService {
    RoleEntity getRoleByEnum(RoleEnum roleEnum);
}