package com.tecno.corralito;

import com.tecno.corralito.models.entities.usuario.PermissionEntity;
import com.tecno.corralito.models.entities.usuario.RoleEntity;
import com.tecno.corralito.models.entities.enums.RoleEnum;
import com.tecno.corralito.models.entities.usuario.UserEntity;
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

        // Constructor para inyectar dependencias
        public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Bean
        CommandLineRunner init() {
            return args -> {
                /* Crear PERMISOS */
                PermissionEntity consultarPermission = PermissionEntity.builder()
                        .name("CONSULTAR")
                        .build();

                PermissionEntity transferirPermission = PermissionEntity.builder()
                        .name("TRANSFERIR")
                        .build();

                PermissionEntity viewAccountPermission = PermissionEntity.builder()
                        .name("VIEW_ACCOUNT")
                        .build();

                PermissionEntity viewTransactionPermission = PermissionEntity.builder()
                        .name("VIEW_TRANSACTION")
                        .build();

                /* Crear ROLES */
                RoleEntity roleAdmin = RoleEntity.builder()
                        .roleEnum(RoleEnum.ADMIN)
                        .permissionList(Set.of(
                                consultarPermission,
                                viewAccountPermission,
                                viewTransactionPermission
                        ))
                        .build();

                RoleEntity roleUser = RoleEntity.builder()
                        .roleEnum(RoleEnum.USER)
                        .permissionList(Set.of(
                                consultarPermission,
                                transferirPermission,
                                viewTransactionPermission
                        ))
                        .build();

                /* CREAR USUARIOS */
                UserEntity userSantiago = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin")) // Codificar la contraseña
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleAdmin))
                        .build();

                UserEntity userDaniel = UserEntity.builder()
                        .username("prueba")
                        .password(passwordEncoder.encode("12345")) // Codificar la contraseña
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleUser))
                        .build();

                userRepository.saveAll(List.of(userSantiago, userDaniel));
            };
        }
    }
}
