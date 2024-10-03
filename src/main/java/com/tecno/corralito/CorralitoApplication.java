package com.tecno.corralito;

import com.tecno.corralito.models.entities.enums.Estado;
import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.PermissionEntity;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.usuario.UserEntity;
import com.tecno.corralito.models.repositories.usuario.RoleRepository;
import com.tecno.corralito.models.repositories.usuario.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class CorralitoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorralitoApplication.class, args);
    }

    @Component
    public class DataInitializer {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;

        // Constructor para inyectar dependencias
        public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.roleRepository = roleRepository;
        }

        @Bean
        CommandLineRunner init() {
            return args -> {
                // Crear PERMISOS
                PermissionEntity consultarPermission = PermissionEntity.builder()
                        .name("CONSULTAR")
                        .build();

                PermissionEntity transferirPermission = PermissionEntity.builder()
                        .name("TRANSFERIR")
                        .build();

                // Guardar permisos en la base de datos
                // Asegúrate de que tienes un PermissionRepository para guardarlos
                // permissionRepository.save(consultarPermission);
                // permissionRepository.save(transferirPermission);

                // Crear ROLES
                RoleEntity roleTurista = RoleEntity.builder()
                        .roleEnum(RoleEnum.turista)
                        .permissionList(Set.of(consultarPermission, transferirPermission))
                        .build();

                // Guardar rol en la base de datos
                roleRepository.save(roleTurista);
            };
        }
    }
}