# Arquitectura del sistema

## Capas

```
Controller (REST) — 12 controladores
    ↕ DTOs (records) — ~45 DTOs
Service (lógica de negocio) — 12 servicios
    ↕ Entidades (JPA) — 12 entidades
Repository (Spring Data) — 9 repositorios
    ↕ ValidacionesHelpers — 8 helpers
Base de datos (MySQL + Flyway) — 22 migraciones
```

## Patrones usados

- **DTO Pattern**: records inmutables por cada operación (registro, lista, detalle, actualización)
- **Helper Pattern**: clases `*ValidacionesHelper` para lógica de validación reutilizable
- **Soft Delete**: columna `activo` booleana en todas las tablas
- **UUID**: identificadores únicos universales generados por Hibernate
- **Cache en servicio**: listas cacheadas en `ClienteService`, `ContratoService`, `CategoriaService` para evitar N+1 en formularios
- **Batch queries**: consultas JPQL con `IN :ids` para obtener counts de notas/cancelaciones por lote en la lista de OC

## Flujo de una petición típica

1. Controller recibe DTO request con `@Valid`
2. Service orquesta la lógica: helpers de validación → creación de entidad → repositorio
3. Repository (JPA) persiste la entidad
4. Controller devuelve DTO response con HTTP status adecuado (201 created, 200 ok, 204 no content)

## Manejo de errores

- `ResourceNotFoundException` → 404
- `RecursoExistenteException` → 409
- Validaciones `@Valid` → 400
- JWT expirado → catch en `JwtAuthFilter`, 401 silencioso (no cuelga)
- Errores no esperados → `@RestControllerAdvice` global

## Seguridad

- JWT Bearer token con jjwt
- 3 roles: ADMIN, USER, VIEWER
- Permisos granulares: módulo × acción por usuario (tabla `usuario_permisos`)
- `AuthGuard` (frontend) protege todas las rutas excepto `/`
- `AdminGuard` protege `/config` y endpoints de administración
- CORS permitido para `localhost:4200`
