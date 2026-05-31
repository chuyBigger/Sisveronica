# Arquitectura del sistema

## Capas

```
Controller (REST)
    ↕ DTOs (records)
Service (lógica de negocio)
    ↕ Entidades (JPA)
Repository (Spring Data)
    ↕ 
Base de datos (MySQL + Flyway)
```

## Patrones usados

- **DTO Pattern**: records inmutables por cada operación (registro, lista, detalle, actualización)
- **Helper Pattern**: clases `*ValidacionesHelper` para lógica de validación reutilizable
- **Strategy Pattern**: `ValidadorDeProductos` interface con implementaciones intercambiables
- **Soft Delete**: columna `activo` booleana en todas las tablas
- **Mapper**: MapStruct para conversiones entidad ↔ DTO (ProductoMapper, OrdenCompraDetalleMapper)
- **UUID**: identificadores únicos universales generados por Hibernate

## Flujo de una petición típica

1. Controller recibe DTO request con `@Valid`
2. Service orquesta la lógica: helpers de validación → creación de entidad → repositorio
3. Repository (JPA) persiste la entidad
4. Controller devuelve DTO response con HTTP status adecuado (201 created, 200 ok, 204 no content)

## Manejo de errores

- `ResourceNotFoundException` → 404
- `RecursoExistenteException` → 409
- Validaciones `@Valid` → 400
- Errores no esperados → `@RestControllerAdvice` global
