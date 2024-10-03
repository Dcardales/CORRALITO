package com.tecno.ctgbank;

import com.tecno.ctgbank.models.entity.enums.Estado;
import com.tecno.ctgbank.models.entity.usuario.PermissionEntity;
import com.tecno.ctgbank.models.entity.usuario.RoleEntity;
import com.tecno.ctgbank.models.entity.enums.RoleEnum;
import com.tecno.ctgbank.models.entity.usuario.UserEntity;
import com.tecno.ctgbank.models.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class CtgbankApplication {

    public static void main(String[] args) {
        SpringApplication.run(CtgbankApplication.class, args);
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
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .estado(Estado.ACTIVO)
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleAdmin))
                        .build();

                UserEntity userDaniel = UserEntity.builder()
                        .email("prueba@gmail.com")
                        .password(passwordEncoder.encode("12345"))
                        .estado(Estado.ACTIVO)
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
