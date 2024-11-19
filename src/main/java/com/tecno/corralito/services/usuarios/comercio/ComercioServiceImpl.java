package com.tecno.corralito.services.usuarios.comercio;

import com.tecno.corralito.exceptions.ResourceNotFoundException;
import com.tecno.corralito.exceptions.UsuarioYaExisteException;
import com.tecno.corralito.mapper.ComercioMapper;
import com.tecno.corralito.models.dto.tiposUsuario.comercio.AuthCreateComercioRequest;
import com.tecno.corralito.models.dto.tiposUsuario.comercio.UpdateComercio;
import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.entity.usuario.tiposUsuarios.Comercio;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import com.tecno.corralito.models.repository.usuario.tiposUsuarios.ComercioRepository;
import com.tecno.corralito.models.response.auth.AuthResponse;
import com.tecno.corralito.models.response.usuarios.comercio.ComercioResponse;
import com.tecno.corralito.models.response.usuarios.comercio.UpdateComercioRequest;
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
public class ComercioServiceImpl implements  IComercioService{

    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ComercioMapper comercioMapper;


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

    @Override
    public UpdateComercio getComercioByUserId(Long userId) {
        Comercio comercio = comercioRepository.findByUsuario_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Comercio no encontrado con userId: " + userId));

        // Usar el mapper para convertir la entidad a DTO
        return comercioMapper.toUpdateComercio(comercio);
    }


    @Transactional
    @Override
    public ComercioResponse actualizarComercio(Integer id, UpdateComercioRequest comercioRequest) {
        // Buscar comercio existente
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

        // Verificar si el nombre del comercio ya está registrado (si se intenta cambiar)
        if (!comercio.getNombreComercio().equals(comercioRequest.getNombreComercio())) {
            Optional<Comercio> existingComercio = comercioRepository.findByNombreComercio(comercioRequest.getNombreComercio());
            if (existingComercio.isPresent()) {
                throw new IllegalArgumentException("El nombre del comercio ya está en uso.");
            }
        }

        // Actualizar los campos del comercio
        comercio.setNombreComercio(comercioRequest.getNombreComercio());
        comercio.setNit(comercioRequest.getNit());
        comercio.setRazonSocial(comercioRequest.getRazonSocial());
        comercio.setDireccionComercio(comercioRequest.getDireccionComercio());
        comercio.setTelefono(comercioRequest.getTelefono());

        // Guardar los cambios
        comercioRepository.save(comercio);

        return new ComercioResponse("Comercio actualizado exitosamente", comercio);
    }


    @Transactional
    @Override
    public void eliminarComercio(Integer id) {
        // Verificar si el comercio existe
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

        // Eliminar el comercio
        comercioRepository.delete(comercio);
    }


    @Override
    public List<Comercio> listarComercios() {
        return comercioRepository.findAll();
    }


    @Override
    public Comercio obtenerComercioPorId(Integer id) {
        return comercioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));
    }


    @Transactional
    @Override
    public void restablecerContraseña(Integer comercioId, String newPassword) {
        // Obtener el comercio
        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new EntityNotFoundException("Comercio no encontrado"));

        // Obtener el usuario asociado al comercio
        UserEntity user = comercio.getUsuario();
        if (user == null) {
            throw new EntityNotFoundException("Usuario no encontrado para este comercio");
        }

        // Validar la nueva contraseña (puedes añadir más validaciones como longitud mínima, complejidad, etc.)
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía.");
        }

        // Actualizar la contraseña
        user.setPassword(passwordEncoder.encode(newPassword));

        // Guardar el usuario con la nueva contraseña
        userRepository.save(user);
    }

}
