package com.tecno.corralito.services.impl;




import com.tecno.corralito.models.dto.usuarios.AuthCreateTuristaRequest;
import com.tecno.corralito.models.entity.tiposUsuarios.Turista;
import com.tecno.corralito.models.entity.usuario.Nacionalidad;
import com.tecno.corralito.models.repository.tiposUsuarios.NacionalidadRepository;
import com.tecno.corralito.models.repository.tiposUsuarios.TuristaRepository;
import com.tecno.corralito.models.request.Auth.AuthCreateUserRequest;
import com.tecno.corralito.models.request.Auth.AuthLoginRequest;
import com.tecno.corralito.models.response.AuthResponse;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.services.IAuthService;
import com.tecno.corralito.services.INacionalidadService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

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
    private TuristaRepository turistaRepository;

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

    public AuthResponse createUser(AuthCreateUserRequest createRoleRequest) {
        String email = createRoleRequest.email();
        String password = createRoleRequest.password();
        String estado = createRoleRequest.estado();

        // Inicializar rolesRequest como una lista vacía si roleRequest es null
        List<String> rolesRequest = (createRoleRequest.roleRequest() != null)
                ? createRoleRequest.roleRequest().roleListName()
                : new ArrayList<>();

        Set<RoleEntity> roleEntityList;

        // Si no se especifican roles, asignar el rol USER automáticamente
        if (rolesRequest == null || rolesRequest.isEmpty()) {
            RoleEntity userRole = roleRepository.findByRoleEnum(RoleEnum.USER)
                    .orElseThrow(() -> new IllegalArgumentException("Default role USER not found."));
            roleEntityList = new HashSet<>();
            roleEntityList.add(userRole);
        } else {
            // Si se especifican roles (como ADMIN), los asignamos
            roleEntityList = roleRepository.findRoleEntitiesByRoleEnumIn(rolesRequest)
                    .stream().collect(Collectors.toSet());

            if (roleEntityList.isEmpty()) {
                throw new IllegalArgumentException("The roles specified do not exist.");
            }
        }

        // Crear el usuario con los roles asignados
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


        // Generar token JWT
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "User created successfully", accessToken, true);
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
        Optional<Nacionalidad> nacionalidadOptional = nacionalidadRepository.findByDescripcion(turistaRequest.getNacionalidad());
        if (nacionalidadOptional.isEmpty()) {
            return new AuthResponse(email, "Nacionalidad no encontrada", null, false);
        }

        // Crear el objeto Turista y asociarlo con el Usuario y la Nacionalidad
        Turista turista = new Turista();
        turista.setNombre(turistaRequest.getNombre());
        turista.setApellidos(turistaRequest.getApellidos());
        turista.setTelefono(turistaRequest.getTelefono());

        turista.setGenero(turistaRequest.getGenero());
        turista.setNacionalidad(nacionalidadOptional.get());
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
}

