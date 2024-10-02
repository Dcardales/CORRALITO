package com.tecno.corralito.services.impl;


import com.tecno.corralito.models.dto.AuthCreateUserRequest;
import com.tecno.corralito.models.dto.AuthLoginRequest;
import com.tecno.corralito.models.dto.AuthResponse;
import com.tecno.corralito.models.entities.cuenta.Cuenta;
import com.tecno.corralito.models.entities.tiposUsuarios.Turista;
import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.UserEntity;
import com.tecno.corralito.models.repositories.usuario.RoleRepository;
import com.tecno.corralito.models.repositories.usuario.UserRepository;
import com.tecno.corralito.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByCorreo(String correo) {

        UserEntity userEntity = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el correo " + correo + " no existe."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles().forEach(role ->
                authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())))
        );

        userEntity.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission ->
                        authorityList.add(new SimpleGrantedAuthority(permission.getName()))
                );

        return new User(
                userEntity.getCorreo(),
                userEntity.getContrasena(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList
        );
    }


    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String correo = authLoginRequest.correo();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(correo, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(correo, "Inicio de sesión exitoso", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String correo, String password) {
        UserDetails userDetails = this.loadUserByUsername(correo);

        if (userDetails == null) {
            throw new BadCredentialsException("Correo o contraseña inválidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(correo, password, userDetails.getAuthorities());
    }

    @Override
    public AuthResponse registerTurista(AuthCreateTuristaRequest createTuristaRequest) {
        String correo = createTuristaRequest.correo();
        String password = createTuristaRequest.password();

        // Verificar si el correo ya está registrado
        if (userRepository.findByCorreo(correo).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado. Por favor, elija otro.");
        }

        // Asignar el rol de TURISTA (rol por defecto)
        RoleEntity turistaRole = roleRepository.findByRoleEnum(RoleEnum.turista)
                .orElseThrow(() -> new IllegalArgumentException("Default role USER not found."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(turistaRole);

        // Crear la entidad Usuario asociada al Turista
        UserEntity userEntity = UserEntity.builder()
                .correo(correo)
                .contrasena(passwordEncoder.encode(password))
                .roles(roleEntityList)
                .isEnabled(true) // Usuario habilitado
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        // Manejo de la nacionalidad
        Nacionalidad nacionalidad = createTuristaRequest.nacionalidad();
        Optional<Nacionalidad> existingNacionalidad = nacionalidadRepository.findByDescripcion(nacionalidad.getDescripcion());
        if (existingNacionalidad.isPresent()) {
            nacionalidad = existingNacionalidad.get(); // Asignar la nacionalidad existente
        } else {
            throw new IllegalArgumentException("Nacionalidad no encontrada.");
        }

        // Crear la entidad Turista
        Turista turista = Turista.builder()
                .userEntity(userEntity) // Asignar el UserEntity recién creado
                .nacionalidad(nacionalidad)
                .nombre(createTuristaRequest.nombre())
                .apellidos(createTuristaRequest.apellidos())
                .genero(createTuristaRequest.genero())
                .telefono(createTuristaRequest.telefono())
                .build();

        // Guardar el turista (esto guardará también al usuario debido a la relación de cascada)
        turistaRepository.save(turista);

        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userEntity.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name()))
        );

        userEntity.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission ->
                        authorities.add(new SimpleGrantedAuthority(permission.getName()))
                );

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(correo, "Turista creado exitosamente", accessToken, true);
    }
}

