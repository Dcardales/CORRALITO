package com.tecno.corralito;

import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.usuario.PermissionEntity;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.repository.usuario.UserRepository;
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

                RoleEntity roleTurista = RoleEntity.builder()
                        .roleEnum(RoleEnum.TURISTA)
                        .permissionList(Set.of(
                                consultarPermission,
                                transferirPermission,
                                viewTransactionPermission
                        ))
                        .build();

                RoleEntity roleEnte = RoleEntity.builder()
                        .roleEnum(RoleEnum.ENTEREGULADOR)
                        .permissionList(Set.of(
                                consultarPermission,
                                transferirPermission,
                                viewTransactionPermission
                        ))
                        .build();

                RoleEntity roleComercio = RoleEntity.builder()
                        .roleEnum(RoleEnum.COMERCIO)
                        .permissionList(Set.of(
                                consultarPermission,
                                transferirPermission,
                                viewTransactionPermission
                        ))
                        .build();

                /* CREAR USUARIOS */
                UserEntity userDaniel = UserEntity.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin"))
                        .estado(Estado.ACTIVO)
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleAdmin))
                        .build();

                UserEntity userTurista = UserEntity.builder()
                        .email("turista@gmail.com")
                        .password(passwordEncoder.encode("12345"))
                        .estado(Estado.ACTIVO)
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleTurista))
                        .build();

                UserEntity userComercio = UserEntity.builder()
                        .email("comercio@gmail.com")
                        .password(passwordEncoder.encode("12345"))
                        .estado(Estado.ACTIVO)
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleComercio))
                        .build();

                UserEntity userEnte = UserEntity.builder()
                        .email("ente@gmail.com")
                        .password(passwordEncoder.encode("12345"))
                        .estado(Estado.ACTIVO)
                        .isEnabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(true)
                        .credentialNoExpired(true)
                        .roles(Set.of(roleEnte))
                        .build();

                userRepository.saveAll(List.of(userDaniel, userTurista, userEnte, userComercio));
            };
        }
    }
}
