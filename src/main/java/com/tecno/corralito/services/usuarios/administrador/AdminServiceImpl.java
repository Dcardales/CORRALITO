package com.tecno.corralito.services.usuarios.administrador;

import com.tecno.corralito.exceptions.AdministradorNotFoundException;
import com.tecno.corralito.exceptions.UsuarioYaExisteException;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.CreateAdminRequest;
import com.tecno.corralito.models.dto.tiposUsuario.administrador.UpdateAdminRequest;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Administrador;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.AdministradorRepository;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.util.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    @Override
    public AuthResponse registerAdministrador(CreateAdminRequest adminRequest) {
        String email = adminRequest.getEmail();

        // Verificar si el correo ya está registrado
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UsuarioYaExisteException("El usuario ya existe");
        }

        // Asignar rol y crear usuario como en tu código
        RoleEntity adminRole = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("Rol ADMIN no encontrado."));
        Set<RoleEntity> roleEntityList = new HashSet<>();
        roleEntityList.add(adminRole);

        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(adminRequest.getPassword()))
                .estado(Estado.ACTIVO)
                .roles(roleEntityList)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userSaved = userRepository.save(userEntity);

        Administrador admin = new Administrador();
        admin.setTipoIdentificacion(adminRequest.getTipoIdentificacion());
        admin.setIdentificacion(adminRequest.getIdentificacion());
        admin.setNombre(adminRequest.getNombre());
        admin.setApellidos(adminRequest.getApellidos());
        admin.setTelefono(adminRequest.getTelefono());
        admin.setUsuario(userSaved);

        administradorRepository.save(admin);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleEnum().name())));
        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "Administrador registrado exitosamente", accessToken, true);
    }

    @Transactional
    @Override
    public Administrador updateAdministrador(Integer id, UpdateAdminRequest adminRequest) {
        Administrador existingAdmin = administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con ID: " + id));

        existingAdmin.setTipoIdentificacion(adminRequest.getTipoIdentificacion());
        existingAdmin.setIdentificacion(adminRequest.getIdentificacion());
        existingAdmin.setNombre(adminRequest.getNombre());
        existingAdmin.setApellidos(adminRequest.getApellidos());
        existingAdmin.setTelefono(adminRequest.getTelefono());

        return administradorRepository.save(existingAdmin);
    }

    @Transactional
    public void deleteAdministrador(Integer id) {
        // Obtener el administrador por ID
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con ID: " + id));

        // Eliminar el usuario asociado al administrador
        UserEntity user = admin.getUsuario();
        if (user != null) {
            // Limpiar la relación de roles antes de eliminar el usuario
            user.getRoles().clear();
            userRepository.save(user);  // Asegurarse de guardar cambios en la relación de roles

            // Eliminar el usuario
            userRepository.delete(user);
        }

        // Finalmente, eliminar el administrador
        administradorRepository.delete(admin);
    }

    @Override
    public List<Administrador> getAllAdministradores() {
        return administradorRepository.findAll();
    }

    @Override
    public Administrador getAdministradorById(Integer id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador no encontrado con ID: " + id));
    }
}
