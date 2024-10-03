package com.tecno.corralito.services.impl;

import com.tecno.corralito.models.dto.authDTO.AuthCreateTuristaRequest;
import com.tecno.corralito.models.dto.authDTO.AuthLoginRequest;
import com.tecno.corralito.models.dto.authDTO.AuthResponse;
import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.tiposUsuarios.Turista;
import com.tecno.corralito.models.entities.usuario.Nacionalidad;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.usuario.UserEntity;
import com.tecno.corralito.models.repositories.tiposUsuarios.TuristaRepository;
import com.tecno.corralito.models.repositories.usuario.RoleRepository;
import com.tecno.corralito.models.repositories.usuario.UserRepository;
import com.tecno.corralito.services.IAuthService;
import com.tecno.corralito.services.IJwtService;
import com.tecno.corralito.services.INacionalidadService;
import com.tecno.corralito.services.IRoleService;
import com.tecno.corralito.util.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImpl implements IAuthService, UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TuristaRepository turistaRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private INacionalidadService nacionalidadService;

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserValidationService userValidationService;


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

        // Aquí asegúrate de utilizar `authentication` que es de tipo `UsernamePasswordAuthenticationToken`
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

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


    @Override
    @Transactional // Manejo de transacciones
    public AuthResponse registerTurista(AuthCreateTuristaRequest authCreateTuristaRequest) {
        String correo = authCreateTuristaRequest.correo();
        String password = authCreateTuristaRequest.password();

        // Verificar si el correo ya está registrado
        userValidationService.validateUniqueCorreo(correo);

        // Asignar el rol de TURISTA (rol por defecto)
        RoleEntity turistaRole = roleService.getRoleByEnum(RoleEnum.turista);
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(turistaRole);

        // Crear la entidad Usuario asociada al Turista
        UserEntity userEntity = createUserEntity(correo, password, roleEntityList);

        // Manejo de la nacionalidad
        Nacionalidad nacionalidad = nacionalidadService.getNacionalidadByDescripcion(authCreateTuristaRequest.nacionalidad().descripcion());


        // Asignar la nacionalidad al turista
        Turista turista = createTurista(authCreateTuristaRequest, nacionalidad, userEntity);

        // Guardar el turista (esto guardará también al usuario debido a la relación de cascada)
        turistaRepository.save(turista);

        // Generar token JWT
        String accessToken = jwtService.createToken((Authentication) userEntity);

        return new AuthResponse(correo, "Turista creado exitosamente", accessToken, true);
    }

    private UserEntity createUserEntity(String correo, String password, Set<RoleEntity> roles) {
        return UserEntity.builder()
                .correo(correo)
                .contrasena(passwordEncoder.encode(password))
                .roles(roles)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();
    }

    private Turista createTurista(AuthCreateTuristaRequest request, Nacionalidad nacionalidad, UserEntity userEntity) {
        return Turista.builder()
                .nombre(request.nombre())
                .apellidos(request.apellidos())
                .telefono(request.telefono())
                .genero(request.genero())
                .nacionalidad(nacionalidad)
                .userEntity(userEntity)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

