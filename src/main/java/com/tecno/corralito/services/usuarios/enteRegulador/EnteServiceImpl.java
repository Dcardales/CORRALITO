package com.tecno.corralito.services.usuarios.enteRegulador;

import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.exceptions.UsuarioYaExisteException;
import com.tecno.corralito.mapper.EnteReguladorMapper;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.EnteReguladorDto;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.UpdateEnteRequest;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.repository.usuario.NacionalidadRepository;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.EnteReguladorRepository;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.models.response.usuarios.EnteRegulador.EnteResponse;
import com.tecno.corralito.services.usuarios.turista.INacionalidadService;
import com.tecno.corralito.util.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnteServiceImpl implements  IEnteService{

    @Autowired
    private EnteReguladorRepository enteReguladorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @Autowired
    private INacionalidadService nacionalidadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EnteReguladorMapper enteReguladorMapper;


    @Transactional
    @Override
    public AuthResponse registerEnteRegulador(AuthCreateEnteRequest enteRequest) {
        String email = enteRequest.getEmail();
        String password = enteRequest.getPassword();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UsuarioYaExisteException("El usuario ya existe");
        }

        // Asignar el rol ENTE_REGULADOR automáticamente
        RoleEntity enteRole = roleRepository.findByRoleEnum(RoleEnum.ENTEREGULADOR)
                .orElseThrow(() -> new IllegalArgumentException("Rol ENTE_REGULADOR no encontrado."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(enteRole);

        // Crear el usuario con el rol asignado
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .estado(Estado.ACTIVO)
                .roles(roleEntityList)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userSaved = userRepository.save(userEntity);

        // Crear el objeto EnteRegulador y asociarlo con el Usuario
        EnteRegulador enteRegulador = new EnteRegulador();
        enteRegulador.setTipoIdentificacion(enteRequest.getTipoIdentificacion());
        enteRegulador.setIdentificacion(enteRequest.getIdentificacion());
        enteRegulador.setNombre(enteRequest.getNombre());
        enteRegulador.setApellidos(enteRequest.getApellidos());
        enteRegulador.setTelefono(enteRequest.getTelefono());
        enteRegulador.setUsuario(userSaved);

        // Guardar el ente regulador en la base de datos
        enteReguladorRepository.save(enteRegulador);

        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Ente Regulador registrado exitosamente", accessToken, true);
    }

    @Override
    public EnteReguladorDto getEnteReguladorByUserId(Long userId) {
        EnteRegulador enteRegulador = enteReguladorRepository.findByUsuario_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Ente Regulador no encontrado con userId: " + userId));

        // Convertir entidad a DTO usando el mapper
        return enteReguladorMapper.toEnteReguladorDto(enteRegulador);
    }


    @Transactional
    @Override
    public EnteResponse updateEnteRegulador(Integer id, UpdateEnteRequest enteRequest) {
        // Verificar si el EnteRegulador existe
        EnteRegulador enteRegulador = enteReguladorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ente Regulador no encontrado"));

        // Actualizar la información del EnteRegulador
        enteRegulador.setTipoIdentificacion(enteRequest.getTipoIdentificacion());
        enteRegulador.setIdentificacion(enteRequest.getIdentificacion());
        enteRegulador.setNombre(enteRequest.getNombre());
        enteRegulador.setApellidos(enteRequest.getApellidos());
        enteRegulador.setTelefono(enteRequest.getTelefono());

        enteReguladorRepository.save(enteRegulador);

        return new EnteResponse("Ente Regulador actualizado exitosamente", enteRegulador);
    }


    @Transactional
    @Override
    public void deleteEnteRegulador(Integer id) {
        // Verificar si el EnteRegulador existe
        EnteRegulador enteRegulador = enteReguladorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ente Regulador no encontrado"));

        // Eliminar el EnteRegulador y su Usuario asociado
        userRepository.delete(enteRegulador.getUsuario());
        enteReguladorRepository.delete(enteRegulador);
    }


    @Override
    public List<EnteRegulador> listEnteReguladores() {
        return enteReguladorRepository.findAll();
    }


    @Override
    public EnteRegulador getEnteReguladorById(Integer id) {
        return enteReguladorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ente Regulador no encontrado"));
    }


    @Transactional
    @Override
    public void restablecerContraseña(Integer enteId, String newPassword) {
        // Verificar si el EnteRegulador existe
        EnteRegulador enteRegulador = enteReguladorRepository.findById(enteId)
                .orElseThrow(() -> new EntityNotFoundException("Ente Regulador no encontrado"));

        // Verificar que la nueva contraseña no sea vacía
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser vacía");
        }

        // Obtener el Usuario asociado al EnteRegulador
        UserEntity userEntity = enteRegulador.getUsuario();

        // Actualizar la contraseña
        userEntity.setPassword(passwordEncoder.encode(newPassword));

        // Guardar el cambio
        userRepository.save(userEntity);

    }


}
