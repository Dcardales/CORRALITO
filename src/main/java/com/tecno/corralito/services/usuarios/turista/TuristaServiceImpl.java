package com.tecno.corralito.services.usuarios.turista;


import com.tecno.corralito.exceptions.NacionalidadNotFoundException;
import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.exceptions.UsuarioYaExisteException;
import com.tecno.corralito.models.dto.tiposUsuario.turista.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.TuristaUpdateRequest;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import com.tecno.corralito.models.repository.usuario.NacionalidadRepository;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.TuristaRepository;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.util.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TuristaServiceImpl implements ITuristaService {

    @Autowired
    private TuristaRepository turistaRepository;

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



    @Transactional
    @Override
    public AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest) {
        String email = turistaRequest.getEmail();
        String password = turistaRequest.getPassword();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UsuarioYaExisteException("El usuario ya existe");
        }

        // Asignar el rol TURISTA automáticamente
        RoleEntity turistaRole = roleRepository.findByRoleEnum(RoleEnum.TURISTA)
                .orElseThrow(() -> new IllegalArgumentException("Rol TURISTA no encontrado."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(turistaRole);

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

        // Manejo de la nacionalidad
        Nacionalidad nacionalidad = nacionalidadRepository.findByDescripcion(turistaRequest.getNacionalidad())
                .orElseThrow(() -> new NacionalidadNotFoundException("Nacionalidad " + turistaRequest.getNacionalidad() + " no encontrada"));

        // Crear el objeto Turista y asociarlo con el Usuario y la Nacionalidad
        Turista turista = new Turista();
        turista.setNombre(turistaRequest.getNombre());
        turista.setApellidos(turistaRequest.getApellidos());
        turista.setTelefono(turistaRequest.getTelefono());
        turista.setGenero(turistaRequest.getGenero());
        turista.setNacionalidad(nacionalidad);
        turista.setUsuario(userSaved);

        // Guardar el turista en la base de datos
        turistaRepository.save(turista);

        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Turista registrado exitosamente", accessToken, true);
    }



    @Transactional
    @Override
    public Turista updateTurista(Integer turistaId, TuristaUpdateRequest turistaUpdateRequest) {
        Turista turista = turistaRepository.findById(turistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Turista con ID " + turistaId + " no encontrado"));

        // Actualizar los campos permitidos
        turista.setNombre(turistaUpdateRequest.getNombre());
        turista.setApellidos(turistaUpdateRequest.getApellidos());
        turista.setTelefono(turistaUpdateRequest.getTelefono());
        turista.setGenero(turistaUpdateRequest.getGenero());


        if (turistaUpdateRequest.getNacionalidad() != null) {
            Nacionalidad nacionalidad = nacionalidadRepository.findByDescripcion(turistaUpdateRequest.getNacionalidad())
                    .orElseThrow(() -> new NacionalidadNotFoundException("Nacionalidad no encontrada"));
            turista.setNacionalidad(nacionalidad);
        }

        return turistaRepository.save(turista);
    }


    @Transactional
    @Override
    public void deleteTurista(Integer turistaId) {
        Turista turista = turistaRepository.findById(turistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Turista con ID " + turistaId + " no encontrado"));


        userRepository.deleteById(turista.getUsuario().getId());


        turistaRepository.delete(turista);
    }


    @Transactional
    @Override
    public void resetPassword(Integer turistaId, String newPassword) {
        Turista turista = turistaRepository.findById(turistaId)
                .orElseThrow(() -> new ResourceNotFoundException("Turista con ID " + turistaId + " no encontrado"));

        UserEntity usuario = turista.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(usuario);
    }





}
