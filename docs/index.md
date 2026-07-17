# Documentación del Sistema — SisVeronica

## Mapa de documentos

| Documento | Descripción |
|-----------|-------------|
| [`01-documentacion-tecnica.md`](01-documentacion-tecnica.md) | Arquitectura, modelo de datos, módulos, seguridad, frontend y reglas de negocio |
| [`02-contrato-rest.md`](02-contrato-rest.md) | Contrato completo de la API REST: endpoints, JSON request/response, ejemplos |
| [`03-flujo-desarrollo.md`](03-flujo-desarrollo.md) | Guía de desarrollo local, Docker, OCI/Portainer, testing y convenciones |
| [`04-ajustes-arquitectonicos.md`](04-ajustes-arquitectonicos.md) | Correcciones SOLID/ACID aplicadas al backend |
| [`functions-report.md`](functions-report.md) | Reporte completo de todas las funciones del sistema (backend + frontend) |

## Propósito

Sistema web para control de facturación y órdenes de compra de la carnicería **La Verónica**. Gestiona el flujo completo desde la captura de órdenes semanales hasta la generación de facturas, incluyendo notas de venta diarias, cancelaciones, productos extra y reportes de producción.

## Convenciones usadas

- Los ejemplos JSON usan datos reales del sistema (seed data).
- Los endpoints se presentan con el método HTTP y la ruta exacta.
- `{id}` representa un UUID de 36 caracteres.
- Los permisos se indican como `[PUBLICO]`, `[AUTH]`, `[ADMIN]`.
- Los tipos de datos siguen la nomenclatura Java/TypeScript.

## Stack tecnológico

| Componente | Tecnología |
|------------|------------|
| Backend | Spring Boot 3.5.4, Java 21, Maven |
| Frontend | Angular 21, Angular Material 21, TypeScript 5.9 |
| Base de datos | MySQL 8, Flyway (migraciones) |
| Autenticación | JWT (jjwt 0.12.6), BCrypt |
| Documentación API | Swagger/OpenAPI (springdoc 2.8.6) |
| Contenedores | Docker, `linux/arm64` para OCI Ampere |
| Orquestación | Portainer, docker-compose.yml |
| Proxy | nginx (frontend sirve SPA + proxy reverso al backend) |
