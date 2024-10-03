package com.tecno.ctgbank.services.impl;


import com.tecno.ctgbank.models.dto.AuthCreateUserRequest;
import com.tecno.ctgbank.models.dto.AuthLoginRequest;
import com.tecno.ctgbank.models.dto.AuthResponse;
import com.tecno.ctgbank.models.entity.enums.Estado;
import com.tecno.ctgbank.models.entity.usuario.RoleEntity;
import com.tecno.ctgbank.models.entity.enums.RoleEnum;
import com.tecno.ctgbank.models.entity.usuario.UserEntity;
import com.tecno.ctgbank.models.repository.RoleRepository;
import com.tecno.ctgbank.models.repository.UserRepository;
import com.tecno.ctgbank.util.JwtUtils;
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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
}
