package com.tecno.corralito.services.usuarios.auth;


import com.tecno.corralito.exceptions.NacionalidadNotFoundException;
import com.tecno.corralito.exceptions.UsuarioYaExisteException;
import com.tecno.corralito.models.dto.tiposUsuario.comercio.AuthCreateComercioRequest;
import com.tecno.corralito.models.dto.tiposUsuario.enteRegulador.AuthCreateEnteRequest;
import com.tecno.corralito.models.dto.tiposUsuario.turista.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.Auth.AuthLoginRequest;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.EnteRegulador;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Turista;
import com.tecno.corralito.models.repository.usuario.NacionalidadRepository;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.AdministradorRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.ComercioRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.EnteReguladorRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.TuristaRepository;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.services.usuarios.administrador.IAdminService;
import com.tecno.corralito.services.usuarios.turista.INacionalidadService;
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

    @Autowired
    private IAdminService adminService;


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
        AuthResponse authResponse = new AuthResponse(email, "Inicio de sesión exitoso", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Correo electronico o contraseña no válidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
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
    public AuthResponse registerComercio(AuthCreateComercioRequest comercioRequest) {
        String email = comercioRequest.getEmail();
        String password = comercioRequest.getPassword();
        String nombreComercio = comercioRequest.getNombreComercio();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UsuarioYaExisteException("El usuario ya existe");
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
    public AuthResponse registerAdministrador(CreateAdminRequest adminRequest) {
        return adminService.registerAdministrador(adminRequest);
    }


}

