package com.tecno.corralito.validations;


import com.tecno.corralito.models.dto.ResponseDTO;
import com.tecno.corralito.models.entities.usuario.UserEntity;

public class UserValidations {

    public ResponseDTO validate(UserEntity userEntity) {
        ResponseDTO response = new ResponseDTO();
        response.setNumOfErrors(0);

        // Validación de correo
        if (userEntity.getCorreo() == null || userEntity.getCorreo().isEmpty() ||
                !userEntity.getCorreo().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo correo no es correcto.");
        }

        // Validación de contraseña
        if (userEntity.getContrasena() == null || userEntity.getContrasena().isEmpty() ||
                !userEntity.getContrasena().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$")) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("La contraseña debe tener entre 8 y 16 caracteres, al menos un número, una minúscula y una mayúscula.");
        }

        // Validación de estado (si aplica)
        if (userEntity.getEstado() == null) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo estado no puede ser nulo.");
        }

        // Validación de rol (si aplica)
        if (userEntity.getRoles() == null) {
            response.setNumOfErrors(response.getNumOfErrors() + 1);
            response.setMessage("El campo rol no puede ser nulo.");
        }


        return response;
    }
}