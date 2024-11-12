package com.tecno.corralito;


import com.tecno.corralito.models.entity.enums.Estado;
import com.tecno.corralito.models.entity.enums.RoleEnum;
import com.tecno.corralito.models.entity.productoGeneral.Categoria;
import com.tecno.corralito.models.entity.productoGeneral.Producto;
import com.tecno.corralito.models.entity.productoGeneral.Zona;
import com.tecno.corralito.models.entity.usuario.PermissionEntity;
import com.tecno.corralito.models.entity.usuario.RoleEntity;
import com.tecno.corralito.models.entity.usuario.UserEntity;
import com.tecno.corralito.models.repository.PermissionRepository;
import com.tecno.corralito.models.repository.productoGeneral.CategoriaRepository;
import com.tecno.corralito.models.repository.productoGeneral.ProductoRepository;
import com.tecno.corralito.models.repository.productoGeneral.ZonaRepository;
import com.tecno.corralito.models.repository.usuario.RoleRepository;
import com.tecno.corralito.models.repository.usuario.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
        private final CategoriaRepository categoriaRepository;
        private final ProductoRepository productoRepository;
        private final ZonaRepository zonaRepository;
        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;

        // Constructor para inyectar dependencias
        public DataInitializer(
                UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                CategoriaRepository categoriaRepository,
                ProductoRepository productoRepository,
                ZonaRepository zonaRepository,
                RoleRepository roleRepository,
                PermissionRepository permissionRepository) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.categoriaRepository = categoriaRepository;
            this.productoRepository = productoRepository;
            this.zonaRepository = zonaRepository;
            this.roleRepository = roleRepository;
            this.permissionRepository = permissionRepository;
        }

        @Bean
        CommandLineRunner init() {
            return args -> {
                /* Crear y guardar PERMISOS */
                PermissionEntity consultarPermission = permissionRepository.save(
                        PermissionEntity.builder().name("CATEGORIAS").build());

                PermissionEntity transferirPermission = permissionRepository.save(
                        PermissionEntity.builder().name("PRODUCTOS").build());

                PermissionEntity viewAccountPermission = permissionRepository.save(
                        PermissionEntity.builder().name("ZONAS").build());

                PermissionEntity viewTransactionPermission = permissionRepository.save(
                        PermissionEntity.builder().name("VIEW_PRODUCTOS").build());

                /* Crear y guardar ROLES */
                RoleEntity roleAdmin = roleRepository.save(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.ADMIN)
                                .permissionList(Set.of(consultarPermission, viewAccountPermission, viewTransactionPermission))
                                .build());

                RoleEntity roleUser = roleRepository.save(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.USER)
                                .permissionList(Set.of(consultarPermission, transferirPermission, viewTransactionPermission))
                                .build());

                RoleEntity roleTurista = roleRepository.save(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.TURISTA)
                                .permissionList(Set.of(consultarPermission, transferirPermission, viewTransactionPermission))
                                .build());

                RoleEntity roleEnte = roleRepository.save(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.ENTEREGULADOR)
                                .permissionList(Set.of(consultarPermission, transferirPermission, viewTransactionPermission))
                                .build());

                RoleEntity roleComercio = roleRepository.save(
                        RoleEntity.builder()
                                .roleEnum(RoleEnum.COMERCIO)
                                .permissionList(Set.of(consultarPermission, transferirPermission, viewTransactionPermission))
                                .build());

                /* Crear USUARIOS y asignar roles previamente guardados */
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

                /* CREAR CATEGORIAS */
                Categoria categoriaBebidas = new Categoria();
                categoriaBebidas.setNombreCategoria("Bebidas");
                categoriaBebidas.setDescripcionCategoria("Productos liquidos");

                Categoria categoriaComida = new Categoria();
                categoriaComida.setNombreCategoria("Comida");
                categoriaComida.setDescripcionCategoria("Productos Alimenticios ");

                categoriaRepository.saveAll(List.of(categoriaBebidas, categoriaComida));

                /* CREAR ZONAS */
                Zona zonaCentro = new Zona();
                zonaCentro.setNombreZona("Centro");
                zonaCentro.setDescripcionZona("Zona céntrica de la ciudad");

                Zona zonaNorte = new Zona();
                zonaNorte.setNombreZona("Norte");
                zonaNorte.setDescripcionZona("Zona norte de la ciudad");

                zonaRepository.saveAll(List.of(zonaCentro, zonaNorte));

                /* CREAR PRODUCTOS */
                Producto productoAgua = new Producto();
                productoAgua.setNombreProducto("Agua");
                productoAgua.setPrecioMin(new BigDecimal("1.000"));
                productoAgua.setPrecioMax(new BigDecimal("5.000"));
                productoAgua.setCategoria(categoriaBebidas);
                productoAgua.setZona(zonaCentro);

                Producto productoMojarra = new Producto();
                productoMojarra.setNombreProducto("Mojarra");
                productoMojarra.setPrecioMin(new BigDecimal("30.000"));
                productoMojarra.setPrecioMax(new BigDecimal("60.000"));
                productoMojarra.setCategoria(categoriaComida);
                productoMojarra.setZona(zonaNorte);

                productoRepository.saveAll(List.of(productoAgua, productoMojarra));
            };
        }
    }
}