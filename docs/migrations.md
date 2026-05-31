# Migraciones Flyway

Todas las migraciones en `src/main/resources/db/migration/` usando nomenclatura `V{numero}__{descripcion}.sql`.

## Orden de ejecución

| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `V1__create_clientes_table.sql` | Tabla `clientes` |
| 2 | `V2__create_contratos_table.sql` | Tabla `contratos` con FK → clientes |
| 3 | `V3__create_productos_table.sql` | Tabla `productos` con FK → categorias |
| 4 | `V4__create_categorias_table.sql` | Tabla `categorias` |
| 5 | `V5__create_notaventas_table.sql` | Tabla `nota_ventas` con FK → clientes, contratos |
| 6 | (omitido) | V6 no existe |
| 7 | `V7__create_nota_venta_detalles_table.sql` | Tabla `nota_venta_detalles` con FK → productos, nota_ventas |
| 8 | `V8__create_orden_compras.sql` | Tabla `orden_compras` con FK → clientes, contratos |
| 9 | `V9__create_ordencompradetalle.sql` | Tabla `orden_compra_detalles` con FK → orden_compras |
| 10 | `V10__agregar_producto_id_a_orden_compra_detalles.sql` | ALTER para columna `producto_id` FK → productos |
| 11 | `V11__add_orden_compra_id_to_nota_ventas.sql` | ALTER para columna `orden_compra_id` FK → orden_compras |
| 12 | `V12__seed_data.sql` | Datos de prueba (categorías, clientes, productos, contratos, órdenes, notas) |

## Convenciones SQL

- **IDs**: `VARCHAR(36)` para UUIDs, sin AUTO_INCREMENT
- **FKs**: definidas con `@JoinColumn` en JPA, no con CONSTRAINTs explícitas en SQL
- **Enums**: almacenados como `VARCHAR` con el nombre del enum
- **Soft delete**: columna `activo BOOLEAN NOT NULL DEFAULT TRUE`
- **Seed data**: UUIDs explícitos con prefijos por entidad para legibilidad
  - `a1b2...` → Productos
  - `b1b2...` → Categorías
  - `c1b2...` → Clientes
  - `d1b2...` → Contratos
  - `e1b2...` → Órdenes de compra
  - `e2b2...` → Detalles de orden
  - `f1b2...` → Notas de venta
  - `f2b2...` → Detalles de nota

## Notas

- Si se cambia una migración existente, hay que dropear la BD y recrearla (los checksums de Flyway fallarán)
- No se debe modificar una migración ya aplicada en producción
- Las migraciones son idempotentes gracias al versionado de Flyway
