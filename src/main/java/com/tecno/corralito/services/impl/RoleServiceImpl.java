package com.tecno.corralito.services.impl;

import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.repositories.usuario.RoleRepository;
import com.tecno.corralito.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //Método para obtener un rol por su enumeración

    @Override
    public RoleEntity getRoleByEnum(RoleEnum roleEnum) {
        return roleRepository.findByRoleEnum(roleEnum)
                .orElseThrow(() -> new IllegalArgumentException("Rol " + roleEnum.name() + " no encontrado."));
    }

}
