# SisVeronica

Sistema de control de insumos y administración para cocinas económicas, comedores industriales y restaurantes.

## Stack tecnológico

| Tecnología | Versión |
|------------|---------|
| Java | 17 |
| Spring Boot | 3.5.4 |
| Spring Data JPA | Hibernate 6 |
| Flyway | 11.7.2 |
| MySQL | 8.0+ |
| Lombok | Última |
| MapStruct | Última |
| Maven | Wrapper incluido |

## Requisitos

- **Java 17+** (JDK)
- **MySQL 8.0+**
- **Maven** (usar `mvnw.cmd` incluido)

## Configuración

### Base de datos

```sql
CREATE DATABASE sis_veronica;
```

Las credenciales se configuran en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sis_veronica
spring.datasource.username=root
spring.datasource.password=tu_password
```

### Ejecución

```bash
.\mvnw.cmd clean spring-boot:run
```

Flyway ejecuta las migraciones automáticamente al iniciar.

## Estructura del proyecto

```
src/main/java/com/laveronica/siscontrol/
├── controller/          # Controladores REST
├── domain/
│   ├── categoria/       # CRUD categorías + DTOs
│   ├── clientes/        # CRUD clientes + DTOs
│   ├── contratos/       # CRUD contratos + DTOs
│   ├── notaventa/       # Notas de venta + DTOs
│   ├── notaventadetalle/# Detalle de notas + DTOs
│   ├── ordencompra/     # Órdenes de compra + DTOs
│   ├── ordencompradetalle/ # Detalle de órdenes + DTOs
│   └── productos/       # CRUD productos + DTOs
├── enums/               # Partida, UnidadMedida, DiaSemana
├── infra/exceptions/    # Manejo global de errores
├── repositories/        # Interfaces JPA
├── services/            # Lógica de negocio
└── utils/helpers/       # Validaciones reutilizables

src/main/resources/db/migration/  # Migraciones Flyway (SQL)
```

## Modelo de datos

Todas las entidades usan **UUID** como identificador primario (`VARCHAR(36)`) generado automáticamente por Hibernate.

| Entidad | Tabla | IDs foráneos |
|---------|-------|-------------|
| Cliente | `clientes` | — |
| Categoria | `categorias` | — |
| Producto | `productos` | `categoria_id` → Categoria |
| Contrato | `contratos` | `cliente_id` → Cliente |
| OrdenCompra | `orden_compras` | `cliente_id` → Cliente, `contrato_id` → Contrato |
| OrdenCompraDetalle | `orden_compra_detalles` | `orden_compra_id` → OrdenCompra, `producto_id` → Producto |
| NotaVenta | `nota_ventas` | `cliente_id` → Cliente, `contrato_id` → Contrato, `orden_compra_id` → OrdenCompra |
| NotaVentaDetalle | `nota_venta_detalles` | `notaventa_id` → NotaVenta, `producto_id` → Producto |

## API Endpoints

### Productos
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/productos` | Crear producto |
| GET | `/productos` | Listar productos (paginado) |
| GET | `/productos/{id}` | Buscar por UUID |
| GET | `/productos/partidas/{partida}` | Filtrar por partida |
| GET | `/productos/categorias/{id}` | Filtrar por categoría |
| GET | `/productos/buscar/{nombre}` | Buscar por nombre |
| GET | `/productos/buscar_palabras?q=` | Búsqueda por palabra |
| PATCH | `/productos/{id}` | Actualizar producto |
| DELETE | `/productos/{id}` | Eliminar (baja lógica) |

### Clientes
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/clientes` | Crear cliente |
| GET | `/clientes` | Listar clientes |
| GET | `/clientes/{id}` | Buscar por UUID |
| PATCH | `/clientes/{id}` | Actualizar cliente |
| DELETE | `/clientes/{id}` | Eliminar (baja lógica) |

### Categorías
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/categorias` | Crear categoría |
| GET | `/categorias` | Listar categorías |
| GET | `/categorias/{id}` | Buscar por UUID |
| PATCH | `/categorias/{id}` | Actualizar categoría |
| DELETE | `/categorias/{id}` | Eliminar (baja lógica) |

### Contratos
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/contratos` | Crear contrato |
| GET | `/contratos` | Listar contratos |
| GET | `/contratos/{id}` | Buscar por UUID |
| PATCH | `/contratos/{id}` | Actualizar contrato |
| DELETE | `/contratos/{id}` | Eliminar (baja lógica) |

### Órdenes de compra
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/orden_compra` | Crear orden |
| GET | `/orden_compra` | Listar órdenes (paginado) |
| GET | `/orden_compra/{id}` | Buscar por UUID |
| PATCH | `/orden_compra/{id}` | Actualizar orden |
| DELETE | `/orden_compra/{id}` | Eliminar (baja lógica) |

### Notas de venta
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/notaventas` | Crear nota |
| GET | `/notaventas` | Listar notas (paginado) |
| GET | `/notaventas/{id}` | Buscar por UUID |
| PATCH | `/notaventas/{id}` | Actualizar nota |
| DELETE | `/notaventas/{id}` | Eliminar (baja lógica) |

### Enums
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/enums/partidas` | Listar partidas disponibles |
| GET | `/enums/unidades-medida` | Listar unidades de medida |

## Convenciones

- **IDs**: UUID `VARCHAR(36)` generado por Hibernate (`GenerationType.UUID`)
- **Bajas lógicas**: columna `activo` en todas las tablas (nunca se eliminan registros)
- **Seed data**: Flyway V12 inserta datos de prueba con UUIDs explícitos
- **Enums**: `Partida`, `UnidadMedida`, `DiaSemana` almacenados como string en BD
- **Paginación**: `@PageableDefault` con tamaño 9 o 10 y orden por defecto
