package com.tecno.corralito.services.Usuario.impl;


import com.tecno.corralito.exceptions.NacionalidadNotFoundException;
import com.tecno.corralito.models.dto.Auth.*;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.tiposUsuarios.Administrador;
import com.tecno.corralito.models.entity.tiposUsuarios.Comercio;
import com.tecno.corralito.models.entity.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.entity.tiposUsuarios.Turista;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.repository.tiposUsuarios.*;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.response.AuthResponse;
import com.tecno.corralito.services.Usuario.IAuthService;
import com.tecno.corralito.services.Usuario.INacionalidadService;
import com.tecno.corralito.util.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements UserDetailsService, IAuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EnteReguladorRepository enteReguladorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private TuristaRepository turistaRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @Autowired
    private INacionalidadService nacionalidadService;


    @Override
    public UserDetails loadUserByUsername(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("El usuario " + email + " no existe."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream().flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));


        return new User(userEntity.getEmail(), userEntity.getPassword(), userEntity.isEnabled(), userEntity.isAccountNoExpired(), userEntity.isCredentialNoExpired(), userEntity.isAccountNoLocked(), authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(email, "User loged succesfully", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
    }

    @Transactional
    @Override
    public AuthResponse registerTurista(AuthCreateTuristaRequest turistaRequest) {
        String email = turistaRequest.getEmail();
        String password = turistaRequest.getPassword();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return new AuthResponse(email, "El usuario ya existe", null, false);
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
    public AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest) {
        String email = comercioRequest.getEmail();
        String password = comercioRequest.getPassword();
        String nombreComercio = comercioRequest.getNombreComercio();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return new AuthResponse(email, "El usuario ya existe", null, false);
        }

        // Verificar si el nombre del comercio ya está registrado
        Optional<Comercio> existingComercio = comercioRepository.findByNombreComercio(nombreComercio);
        if (existingComercio.isPresent()) {
            throw new IllegalArgumentException("El nombre del comercio ya está en uso.");
        }

        // Asignar el rol COMERCIO automáticamente
        RoleEntity comercioRole = roleRepository.findByRoleEnum(RoleEnum.COMERCIO)
                .orElseThrow(() -> new IllegalArgumentException("Rol COMERCIO no encontrado."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(comercioRole);

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

        // Crear el objeto Comercio y asociarlo con el Usuario
        Comercio comercio = new Comercio();
        comercio.setNombreComercio(nombreComercio);
        comercio.setNit(comercioRequest.getNit());
        comercio.setRazonSocial(comercioRequest.getRazonSocial());
        comercio.setDireccionComercio(comercioRequest.getDireccionComercio());
        comercio.setTelefono(comercioRequest.getTelefono());
        comercio.setUsuario(userSaved);

        // Guardar el comercio en la base de datos
        comercioRepository.save(comercio);

        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Comercio registrado exitosamente", accessToken, true);
    }


    @Transactional
    @Override
    public AuthResponse registerEnteRegulador(AuthCreateEnteRequest enteRequest) {
        String email = enteRequest.getEmail();
        String password = enteRequest.getPassword();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return new AuthResponse(email, "El usuario ya existe", null, false);
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

    @Transactional
    @Override
    public AuthResponse registerAdministrador(AuthCreateAdminRequest adminRequest) {
        String email = adminRequest.getEmail();
        String password = adminRequest.getPassword();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return new AuthResponse(email, "El usuario ya existe", null, false);
        }

        // Asignar el rol ADMIN automáticamente
        RoleEntity adminRole = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("Rol ADMIN no encontrado."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(adminRole);

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

        // Crear el objeto Administrador y asociarlo con el Usuario
        Administrador admin = new Administrador();
        admin.setTipoIdentificacion(adminRequest.getTipoIdentificacion());
        admin.setIdentificacion(adminRequest.getIdentificacion());
        admin.setNombre(adminRequest.getNombre());
        admin.setApellidos(adminRequest.getApellidos());
        admin.setTelefono(adminRequest.getTelefono());
        admin.setUsuario(userSaved);

        // Guardar el administrador en la base de datos
        administradorRepository.save(admin);

        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Administrador registrado exitosamente", accessToken, true);
    }


}

