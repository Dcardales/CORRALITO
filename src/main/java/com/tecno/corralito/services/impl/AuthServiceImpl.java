package com.tecno.corralito.services.impl;

import com.tecno.corralito.models.dto.authDTO.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.authDTO.AuthLoginRequest;
import com.tecno.corralito.models.dto.authDTO.AuthResponse;
import com.tecno.corralito.models.dto.authDTO.NacionalidadRequest;
import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.tiposUsuarios.Turista;
import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.usuario.UserEntity;
import com.tecno.corralito.models.repositories.tiposUsuarios.TuristaRepository;
import com.tecno.corralito.models.repositories.usuario.NacionalidadRepository;
import com.tecno.corralito.models.repositories.usuario.RoleRepository;
import com.tecno.corralito.models.repositories.usuario.UserRepository;
import com.tecno.corralito.services.IAuthService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class AuthServiceImpl implements IAuthService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TuristaRepository turistaRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;


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
        UserDetails userDetails = this.loadUserByCorreo(correo);

        if (userDetails == null) {
            throw new BadCredentialsException("Correo o contraseña inválidos");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        return new UsernamePasswordAuthenticationToken(correo, password, userDetails.getAuthorities());
    }

    @Override
    public AuthResponse registerTurista(AuthCreateTuristaRequest authCreateTuristaRequest) {
        String correo = authCreateTuristaRequest.correo();
        String password = authCreateTuristaRequest.password();

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
        NacionalidadRequest nacionalidadRequest = authCreateTuristaRequest.nacionalidad();

        // Buscar la nacionalidad por su descripción en la base de datos
        Optional<Nacionalidad> existingNacionalidad = nacionalidadRepository.findByDescripcion(nacionalidadRequest.descripcion());

        Nacionalidad nacionalidad;

        if (existingNacionalidad.isPresent()) {
            nacionalidad = existingNacionalidad.get(); // Asignar la nacionalidad existente
        } else {
            throw new IllegalArgumentException("Nacionalidad no encontrada.");
        }

        // Asignar la nacionalidad al turista
        Turista turista = Turista.builder()
                .nombre(authCreateTuristaRequest.nombre())
                .apellidos(authCreateTuristaRequest.apellidos())
                .telefono(authCreateTuristaRequest.telefono())
                .genero(authCreateTuristaRequest.genero())
                .nacionalidad(nacionalidad) // Asignar la nacionalidad al turista
                .userEntity(userEntity) // Asignar la entidad usuario creada previamente
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

