package com.tecno.corralito.validations;


import com.tecno.corralito.models.entities.enums.Estado;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.usuario.UserEntity;

import java.util.Set;

public class UserValidations {

    public void validate(UserEntity userEntity) {
        validateCorreo(userEntity.getCorreo());
        validateContrasena(userEntity.getContrasena());
        validateEstado(userEntity.getEstado());
        validateRoles(userEntity.getRoles());
    }

    private void validateCorreo(String correo) {
        if (correo == null || correo.isEmpty() ||
                !correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("El campo correo no es correcto.");
        }
    }

    private void validateContrasena(String contrasena) {
        if (contrasena == null || contrasena.isEmpty() ||
                !contrasena.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$")) {
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 16 caracteres, al menos un número, una minúscula y una mayúscula.");
        }
    }

    private void validateEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El campo estado no puede ser nulo.");
        }
    }

    private void validateRoles(Set<RoleEntity> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("El campo rol no puede ser nulo.");
        }
    }
}