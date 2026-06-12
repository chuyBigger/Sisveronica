package com.laveronica.siscontrol.infra.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        var securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token JWT obtenido de /auth/login");
        var securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("SisVeronica API")
                        .version("1.0.0")
                        .description("API del sistema de control de ventas y pedidos por módulos. " +
                                "Operaciones: cliente → contrato → orden-compra → nota-venta → cancelaciones → reportes.")
                        .contact(new Contact()
                                .name("Soporte SisVeronica")
                                .email("soporte@laveronica.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .tags(List.of(
                        new Tag().name("Autenticación").description("Login y registro de usuarios"),
                        new Tag().name("Clientes").description("CRUD de clientes"),
                        new Tag().name("Categorías").description("CRUD de categorías de productos"),
                        new Tag().name("Contratos").description("CRUD de contratos por cliente"),
                        new Tag().name("Productos").description("CRUD de productos + carga por Excel"),
                        new Tag().name("Órdenes de Compra").description("Órdenes semanales: producto × día"),
                        new Tag().name("Notas de Venta").description("Facturación diaria desde órdenes de compra"),
                        new Tag().name("Cancelaciones").description("Notas de cancelación y reconstrucción"),
                        new Tag().name("Enums").description("Catálogos de partidas y unidades de medida"),
                        new Tag().name("Usuarios (Admin)").description("Gestión de usuarios y permisos (solo ADMIN)")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
