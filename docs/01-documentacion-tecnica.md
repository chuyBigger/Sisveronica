# Documentación Técnica — SisVeronica

---

## 1\. Introducción

SisVeronica es un sistema web para la gestión del flujo operativo completo de la carnicería **La Verónica**:

- Captura de órdenes de compra semanales por cliente (matriz productos × días)
- Generación de notas de venta diarias con folio secuencial
- Gestión de cancelaciones con validación y auto-aplicación
- Productos extra (fuera de la orden original) con folio propio
- Facturación (notas − cancelaciones) y facturación de extras
- Reporte de producción semanal por cliente
- Control de usuarios con matriz de permisos módulo × acción

### Usuarios objetivo

- **Administradores**: gestionan catálogos, usuarios, permisos y supervisan todo el flujo
- **Operadores**: capturan órdenes, generan notas, firman documentos
- **Visitantes**: consultan información sin capacidad de modificar

### Contexto operativo

La semana laboral inicia el **martes** y termina el **lunes** siguiente. Cada orden de compra cubre una semana completa para un cliente, con cantidades especificadas por producto para cada uno de los 7 días. Las notas de venta se generan por día y por cliente, y su fecha se deriva de la `fechaInicioSemana` de la orden más un offset según el día (martes=+0, miércoles=+1, ..., lunes=+6).

---

## 2\. Arquitectura General

### Stack

```
Frontend (Angular 21 standalone SPA)
    ↕ HTTP/JSON (REST)
Backend (Spring Boot 3.5.4, Java 17, monolítico)
    ↕ JPA/Hibernate
Base de datos (MySQL 8)
```

### Diagrama de capas

```
┌──────────────────────────────────────────────────┐
│              Frontend Angular 21                  │
│  Componentes standalone → Servicios → Modelos     │
│  AuthGuard / AdminGuard → JWT Interceptor         │
│  Angular Material UI → SCSS (dark mode)          │
└──────────────────────┬───────────────────────────┘
                       │ HTTP (JSON)
                       │ JWT en header Authorization
                       ▼
┌──────────────────────────────────────────────────┐
│              Backend Spring Boot 3.5.4            │
│                                                    │
│  Controller (REST) — 15 controladores              │
│      ↕ DTOs (records) — 43 DTOs                   │
│  Service (lógica de negocio) — 15 servicios        │
│      ↕ Entidades (JPA) — 16 entidades             │
│  Repository (Spring Data) — 12 repositorios        │
│                                                    │
│  Security: JwtAuthFilter → SecurityFilterChain     │
│  JWT (jjwt 0.12.6) → 24h expiry → HMAC-SHA       │
└──────────────────────┬───────────────────────────┘
                       │
                       ▼
┌──────────────────────────────────────────────────┐
│              Base de Datos MySQL 8                │
│  17 tablas, Flyway V1–V17, soft-delete           │
│  UUIDs como PK, FK explícitas en JPA             │
└──────────────────────────────────────────────────┘
```

### Principios de diseño

- **DTO Pattern**: records inmutables Java por cada operación (registro, lista, detalle, actualización)
- **Soft Delete**: columna `activo` booleana en todas las tablas, nunca se eliminan registros
- **UUID**: identificadores únicos universales generados por Hibernate (`GenerationType.UUID`)
- **Cache en servicio**: listas cacheadas en `ClienteService`, `ContratoService`, `CategoriaService` para evitar N+1 en formularios
- **Batch queries**: consultas JPQL con `IN :ids` para obtener conteos de notas/cancelaciones por lote en la lista de OC
- **Standalone components**: Angular 21 sin NgModules, lazy loading vía router

### Flujo de una petición típica

1. Frontend envía HTTP request con JWT en header `Authorization: Bearer <token>`
2. `JwtAuthFilter` valida el token (si es inválido o expirado, continúa sin autenticar)
3. `SecurityFilterChain` verifica el rol requerido para la ruta
4. Controller recibe DTO request con `@Valid`
5. Service orquesta la lógica: helpers de validación → creación de entidad → repositorio
6. Repository (JPA) persiste la entidad en MySQL
7. Controller devuelve DTO response con HTTP status (201/200/204)

### Manejo de errores

| Excepción | HTTP Status | Descripción |
|-----------|-------------|-------------|
| `ResourceNotFoundException` / `EntityNotFoundException` | 404 | Recurso no encontrado |
| `RecursoExistenteException` | 409 | Conflicto (duplicado) |
| Violaciones `@Valid` | 400 | Datos de entrada inválidos |
| JWT expirado | 401 | Catch en `JwtAuthFilter`, no bloquea la cadena |
| Errores no esperados | 500 | Capturados por `@RestControllerAdvice` global |

---

## 3\. Modelo de Datos

### 3.1 Mapa de Relaciones

```
clientes 1──* contratos
clientes 1──* nota_ventas
clientes 1──* orden_compras

contratos 1──* nota_ventas
contratos 1──* orden_compras

categorias 1──* productos

productos 1──* orden_compra_detalles
productos 1──* nota_venta_detalles
productos 1──* nota_cancelacion_detalles
productos 1──* extra_detalles

orden_compras 1──* orden_compra_detalles
orden_compras 1──* nota_ventas
orden_compras 1──* nota_cancelaciones
orden_compras 1──* extras
orden_compras 1──* facturas

nota_ventas 1──* nota_venta_detalles
nota_cancelaciones 1──* nota_cancelacion_detalles
extras 1──* extra_detalles
facturas 1──* factura_detalles

usuarios 1──* usuario_permisos
```

### 3.2 Tablas (17)

#### categorias

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| nombre | VARCHAR(255) | NO | — |
| partida | VARCHAR(100) | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### clientes

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| nombre | VARCHAR(255) | NO | — |
| rfc | VARCHAR(50) UNIQUE | SÍ | — |
| calle | VARCHAR(255) | SÍ | — |
| numero | INT | SÍ | — |
| fraccionamiento | VARCHAR(255) | SÍ | — |
| c_p | VARCHAR(50) | NO | — |
| municipio | VARCHAR(255) | SÍ | — |
| estado | VARCHAR(255) | SÍ | — |
| activo | BOOLEAN | NO | TRUE |

#### productos

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| codigo | VARCHAR(50) UNIQUE | SÍ | — |
| nombre | VARCHAR(255) UNIQUE | NO | — |
| partida | VARCHAR(50) | NO | — |
| categoria_id | VARCHAR(36) FK → categorias | SÍ | — |
| unidad_medida | VARCHAR(50) | NO | — |
| precio_compra | DECIMAL(10,2) | SÍ | — |
| precio_venta | DECIMAL(10,2) | SÍ | — |
| activo | BOOLEAN | NO | TRUE |

#### contratos

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| contrato | VARCHAR(100) UNIQUE | NO | — |
| cliente_id | VARCHAR(36) FK → clientes | NO | — |
| fecha_inicio | DATE | NO | — |
| fecha_termino | DATE | NO | — |
| presupuesto | DECIMAL(12,2) | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### orden_compras

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| cliente_id | VARCHAR(36) FK → clientes | NO | — |
| contrato_id | VARCHAR(36) FK → contratos | NO | — |
| partida | VARCHAR(100) | NO | — |
| fecha_inicio_semana | DATE | NO | — |
| fecha_fin_semana | DATE | NO | — |
| confirmado_por | VARCHAR(100) | SÍ | NULL |
| fecha_confirmacion | DATETIME | SÍ | NULL |
| activo | BOOLEAN | NO | TRUE |

**Unique**: `(cliente_id, partida, fecha_inicio_semana)`

#### orden_compra_detalles

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| orden_compra_id | VARCHAR(36) FK → orden_compras | NO | — |
| producto_id | VARCHAR(36) FK → productos | NO | — |
| fecha | DATE | NO | — |
| lunes | DOUBLE | SÍ | — |
| martes | DOUBLE | SÍ | — |
| miercoles | DOUBLE | SÍ | — |
| jueves | DOUBLE | SÍ | — |
| viernes | DOUBLE | SÍ | — |
| sabado | DOUBLE | SÍ | — |
| domingo | DOUBLE | SÍ | — |
| activo | BOOLEAN | NO | TRUE |

#### nota_ventas

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| folio | INT UNIQUE | NO | — |
| fecha | DATETIME | NO | — |
| cliente_id | VARCHAR(36) FK → clientes | NO | — |
| contrato_id | VARCHAR(36) FK → contratos | SÍ | — |
| orden_compra_id | VARCHAR(36) FK → orden_compras | SÍ | — |
| partida | VARCHAR(100) | NO | — |
| dia | VARCHAR(10) | SÍ | — |
| firmada | BOOLEAN | NO | FALSE |
| detalle | TEXT | SÍ | — |
| total_general | DECIMAL(10,2) | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### nota_venta_detalles

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| cantidad | INTEGER | NO | — |
| producto_id | VARCHAR(36) FK → productos | NO | — |
| precio_venta | DECIMAL(10,2) | NO | — |
| sub_total | DECIMAL(10,2) | NO | — |
| notaventa_id | VARCHAR(36) FK → nota_ventas | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### nota_cancelaciones

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| orden_compra_id | VARCHAR(36) FK → orden_compras | NO | — |
| dia | VARCHAR(10) | NO | — |
| fecha_creacion | DATETIME | NO | — |
| creado_por | VARCHAR(100) | SÍ | — |
| validado_por | VARCHAR(100) | SÍ | — |
| fecha_validacion | DATETIME | SÍ | — |
| activo | BOOLEAN | NO | TRUE |

#### nota_cancelacion_detalles

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| nota_cancelacion_id | VARCHAR(36) FK → nota_cancelaciones | NO | — |
| producto_id | VARCHAR(36) FK → productos | NO | — |
| cantidad_cancelada | DOUBLE | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### facturas

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| folio | INT | NO | — |
| orden_compra_id | VARCHAR(36) FK → orden_compras | NO | — |
| cliente | VARCHAR(255) | NO | — |
| contrato | VARCHAR(255) | SÍ | — |
| partida | VARCHAR(100) | NO | — |
| fecha_creacion | DATETIME | NO | — |
| total_general | DECIMAL(10,2) | NO | — |
| es_extras | BOOLEAN | NO | FALSE |
| activo | BOOLEAN | NO | TRUE |

#### factura_detalles

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| factura_id | VARCHAR(36) FK → facturas | NO | — |
| producto_nombre | VARCHAR(255) | NO | — |
| cantidad_total | DOUBLE | NO | — |
| precio_venta | DECIMAL(10,2) | NO | — |
| subtotal | DECIMAL(10,2) | NO | — |

#### extras

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| orden_compra_id | VARCHAR(36) FK → orden_compras | NO | — |
| dia | VARCHAR(10) | NO | — |
| fecha | DATE | NO | — |
| folio | INT | NO | — |
| firmada | BOOLEAN | NO | FALSE |
| fecha_creacion | DATETIME | SÍ | — |
| creado_por | VARCHAR(100) | SÍ | — |
| activo | BOOLEAN | NO | TRUE |

#### extra_detalles

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| extra_id | VARCHAR(36) FK → extras | NO | — |
| producto_id | VARCHAR(36) FK → productos | NO | — |
| cantidad | DOUBLE | NO | — |
| activo | BOOLEAN | NO | TRUE |

#### usuarios

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| username | VARCHAR(100) UNIQUE | NO | — |
| password | VARCHAR(255) (BCrypt) | NO | — |
| role | VARCHAR(20) | NO | — |
| activo | BOOLEAN | NO | TRUE |
| nombre_completo | VARCHAR(255) | SÍ | — |
| correo | VARCHAR(255) | SÍ | — |
| numero | VARCHAR(50) | SÍ | — |
| cargo | VARCHAR(255) | SÍ | — |

#### usuario_permisos

| Columna | Tipo | Nullable | Default |
|---------|------|----------|---------|
| id | VARCHAR(36) PK | NO | — |
| usuario_id | VARCHAR(36) FK → usuarios | NO | — |
| modulo | VARCHAR(30) | NO | — |
| accion | VARCHAR(20) | NO | — |

**Unique**: `(usuario_id, modulo, accion)`

### 3.3 Enumeraciones

#### Partida

```
CARNES, ABARROTES, LACTEOS, FRUTASYVERDURAS, VARIOS, GENERAL
```

#### UnidadMedida

```
KILO, LITRO, PIEZA, PAQUETE
```

#### Role

```
ADMIN, USER, VIEWER
```

#### Modulo

```
PRODUCTOS, CLIENTES, CONTRATOS, NOTAS_VENTA, ORDENES_COMPRA, REPORTES, USUARIOS
```

#### Accion

```
CREAR, LEER, ACTUALIZAR, ELIMINAR
```

### 3.4 Migraciones Flyway V1–V17

| Migración | Descripción |
|-----------|-------------|
| V1 | `categorias` — CREATE + seed de 11 categorías |
| V2 | `clientes` — CREATE + seed de 6 clientes (5 reales + General) |
| V3 | `productos` — CREATE + seed de 30+ productos |
| V4 | `contratos` — CREATE + seed de 5 contratos |
| V5 | `orden_compras` — CREATE + seed de 5 órdenes |
| V6 | `orden_compra_detalles` — CREATE + seed con cantidades diarias |
| V7 | `nota_ventas` — CREATE + seed de 5 notas |
| V8 | `nota_venta_detalles` — CREATE + seed de detalles |
| V9 | `nota_cancelaciones` — CREATE |
| V10 | `nota_cancelacion_detalles` — CREATE |
| V11 | `facturas` — CREATE |
| V12 | `factura_detalles` — CREATE |
| V13 | `usuarios` — CREATE + seed de 3 usuarios (admin, usuario, visita) con BCrypt |
| V14 | `usuario_permisos` — CREATE + seed de permisos para cada rol |
| V15 | `extras` — CREATE |
| V16 | `extra_detalles` — CREATE |
| V17 | ALTER `facturas` ADD `es_extras` |

**Convenciones SQL:**
- IDs: `VARCHAR(36)` para UUIDs, sin AUTO_INCREMENT
- FKs: definidas con `@JoinColumn` en JPA, con CONSTRAINTs explícitas en SQL
- Enums: almacenados como `VARCHAR` con el nombre del enum
- Soft delete: columna `activo BOOLEAN NOT NULL DEFAULT TRUE`
- Seed data: UUIDs explícitos con prefijos por entidad (`a1b2...` productos, `b1b2...` categorías, etc.)
- Las migraciones no deben modificarse después de aplicadas en producción

---

## 4\. Módulos — Explicación Detallada

### 4.1 Auth

**Propósito:** Autenticación y registro de usuarios mediante JWT.

**Reglas de negocio:**
- El login requiere username + password, devuelve un JWT con expiración de 24 horas
- El registro crea un nuevo usuario (solo accesible vía endpoint, no desde UI)
- Las contraseñas se almacenan con BCrypt
- El token JWT se envía en cada request como `Authorization: Bearer <token>`
- Si el token expira, el interceptor redirige al login

**Entradas:**
- `POST /auth/login`: `{ "username": "admin", "password": "admin123" }`
- `POST /auth/register`: `{ "username": "...", "password": "...", "role": "USER" }`

**Salidas:**
- `{ "token": "eyJ...", "username": "admin", "role": "ADMIN", "tipo": "Bearer" }`

**Interacciones:** Usado por todos los demás módulos (todas las rutas excepto `/auth/**` y `/enums/**` requieren autenticación).

---

### 4.2 Catálogos (Categorías, Productos, Clientes, Contratos)

**Propósito:** CRUD completo de las entidades base del sistema.

**Reglas de negocio:**
- **Categorías**: nombre único por partida, soft-delete
- **Productos**: nombre único, código único (auto-generado si no se provee), FK a categoría, partida y unidad de medida obligatorios, soft-delete
- **Clientes**: RFC único (validado con expresión regular), soft-delete
- **Contratos**: nombre único, FK a cliente, fechas inicio/fin, presupuesto decimal(12,2), soft-delete

**Carga por Excel (Productos):**
- Endpoint `POST /productos/excel/cargar` recibe archivo `.xlsx`
- Formato esperado: columnas Nombre, Partida, Categoría, Unidad Medida, Precio Compra, Precio Venta
- Valida cada fila: nombre único, precio presente, categoría existente
- Devuelve reporte con: total procesados, exitosos, duplicados, sin precio

**Entradas:** Formularios HTML con campos validados.

**Salidas:** DTOs con datos de la entidad + HTTP status.

---

### 4.3 Órdenes de Compra

**Propósito:** Gestión de órdenes de compra semanales con matriz de productos × días.

**Reglas de negocio:**
- La semana laboral va de **martes a lunes**
- `fechaInicioSemana` = el martes de la semana
- `fechaFinSemana` = el lunes siguiente (fechaInicioSemana + 6 días)
- No puede haber dos órdenes activas para el mismo (cliente, partida, semana)
- Cada detalle tiene cantidades para los 7 días (lunes–domingo), aunque el valor del campo `fecha` en el detalle es la fecha real del día
- La orden puede estar **pendiente** (recién creada) o **confirmada** (con confirmadoPor y fechaConfirmacion)
- Una vez confirmada, se pueden generar notas desde ella
- Una vez facturada, la orden queda bloqueada (no se pueden modificar notas)

**Estados de OC (calculados en backend):**

| Estado | Condición |
|--------|-----------|
| PENDIENTE | Sin confirmar |
| FIRMAS_PENDIENTES | Confirmada pero con notas sin firmar |
| CANCELACIONES_PENDIENTES | Confirmada con cancelaciones sin validar |
| PREFACTURA | Confirmada, todas las notas firmadas, todas las cancelaciones validadas |
| LISTO | Ya tiene factura generada |

**Entradas:**
- `fechaInicioSemana` (Date), `cliente_id`, `contrato_id`, `partida`
- Lista de detalles con: `fecha`, `producto`, `lunes..domingo` (cantidades)

**Procesamiento:**
- Valida unicidad del par (cliente, partida, semana)
- Calcula `fechaFinSemana` = `fechaInicioSemana + 6 días`
- Crea la orden con cascade de detalles
- La confirmación registra el usuario y timestamp
- La generación de notas crea una NotaVenta por día con los productos del día correspondiente

**Salidas:** DTO completo con detalles, totales, estado y conteos.

---

### 4.4 Notas de Venta

**Propósito:** Facturación diaria: generación desde orden, firma, y registro de incidencias.

**Reglas de negocio:**
- **Folio secuencial global**: `SELECT COALESCE(MAX(folio), 0) + 1` sobre toda la tabla `nota_ventas`
- **Fecha derivada**: la fecha de la nota se calcula como `orden.fechaInicioSemana + offset(día)`, donde martes=+0, miércoles=+1, ..., lunes=+6. Nunca se usa `now()`.
- **Firma**: una nota firmada (`firmada = true`) no puede modificarse ni eliminarse
- **Detalle**: campo TEXT para registrar incidencias (ej. "llegó tarde", "producto faltante")
- **Print**: vista previa en tamaño 1/4 de carta (108×140mm) para ≤8 productos, 1/3 (108×186mm) para >8
- **Bloqueo**: si la OC ya está facturada, no se pueden modificar notas
- **Calidad**: en la lista de notas se puede filtrar por "Con detalle" / "Sin detalle"

**Entradas:**
- Desde OC: `{ "ordenCompraId": "...", "dia": "martes" }`
- Manual: `{ "clienteId": "...", "partida": "CARNES", "detalles": [...] }`

**Procesamiento:**
- Calcula folio auto-incremental
- Para notas desde orden: toma las cantidades del día específico de la OC, las multiplica por precio_venta del producto
- Calcula total general = suma de subtotales
- La firma cambia `firmada` a `true`

**Salidas:** DTO con folio, fecha, cliente, detalles (cantidad, producto, precio, subtotal), total general, estado de firma.

---

### 4.5 Cancelaciones

**Propósito:** Gestión de cancelaciones de productos por día, con validación que auto-aplica las restas a las notas existentes.

**Reglas de negocio:**
- Las cancelaciones se crean por día para una orden específica
- Una cancelación tiene dos estados: **pendiente** (solo creada) y **validada** (con validadoPor y fechaValidacion)
- **Al validar**: automáticamente busca la NotaVenta del mismo (orden, día) y **resta las cantidades canceladas** de los productos correspondientes en la nota
- Si no existe NotaVenta para ese día, la validación falla
- Una cancelación validada no puede eliminarse
- Independiente del flujo de facturación (no bloquea la OC)

**Entradas:**
- `{ "ordenCompraId": "...", "dia": "martes", "detalles": [{"productoId": "...", "cantidadCancelada": 5}] }`

**Procesamiento:**
- Creación: guarda la cancelación con `validadoPor = null`
- Validación: busca NotaVenta by (ordenCompraId, dia), itera los detalles, resta `cantidadCancelada` de `cantidad` en el detalle correspondiente
- Si la cantidad resultante es 0 o negativa, establece la cantidad en 0
- Recalcula subtotales y total general de la nota afectada
- Registra el usuario y timestamp de validación

**Salidas:** DTO con datos de cancelación + estado de validación.

---

### 4.6 Extras

**Propósito:** Productos adicionales fuera de la orden original, pero de la misma partida. Tienen su propio flujo de folio y firma.

**Reglas de negocio:**
- Los productos disponibles para extras se cargan desde la **partida** completa (catálogo), no están limitados a los productos de la OC
- **Folio secuencial propio**: `SELECT COALESCE(MAX(folio), 0) + 1` sobre la tabla `extras`
- **Fecha derivada**: misma regla que NotaVenta (derivada de `orden.fechaInicioSemana + offset`)
- **Firma**: un extra firmado no puede eliminarse
- Se muestran en la sección de detalle de la OC (entre cancelaciones y notas)
- Los extras firmados alimentan la **Factura de Extras**

**Entradas:**
- `{ "ordenCompraId": "...", "dia": "martes", "detalles": [{"productoId": "...", "cantidad": 10}] }`

**Procesamiento:**
- Calcula folio auto-incremental
- Determina la fecha según la regla de offset
- La firma cambia `firmada` a `true`
- Al eliminar (si no está firmado): soft-delete

**Salidas:** DTO con folio, fecha, detalles, estado de firma.

---

### 4.7 Facturas

**Propósito:** Consolidación financiera: suma de notas − cancelaciones = factura. También genera factura separada para extras.

**Reglas de negocio:**
- **Factura principal**: suma todos los productos de todas las NotaVenta de una OC, resta las cantidades canceladas (validadas)
- **Factura de Extras**: suma todos los productos de todos los Extras firmados de una OC, toma los precios de las NotaVenta
- **Condiciones para facturar**:
  - La OC debe estar confirmada
  - Todas las notas deben estar firmadas
  - Todas las cancelaciones deben estar validadas
- **Bloqueo**: una vez generada la factura, la OC queda bloqueada (no se pueden modificar notas, cancelaciones, etc.)
- **Folio secuencial**: `SELECT COALESCE(MAX(folio), 0) + 1` sobre `facturas`
- Las facturas tienen datos denormalizados (cliente, contrato, partida como strings)
- `esExtras = true` distingue factura de extras de la factura principal
- Una OC puede tener máximo 1 factura principal y 1 factura de extras

**Entradas:**
- Factura principal: `{ "ordenCompraId": "..." }`
- Factura de extras: `{ "ordenCompraId": "..." }`

**Procesamiento:**
- Factura principal: agrupa detalles de todas las notas por producto → suma cantidades → resta cancelaciones validadas → calcula subtotales → total general
- Factura de extras: agrupa detalles de todos los extras firmados por producto → busca precio desde notas → calcula subtotales → total general

**Salidas:** DTO con folio, cliente, contrato, partida, detalles (producto, cantidad total, precio, subtotal), total general, esExtras.

---

### 4.8 Reportes

**Propósito:** Reporte de Producción Carnes semanal, agrupado por cliente y día.

**Reglas de negocio:**
- Solo procesa órdenes de la partida **CARNES**
- Agrupa por **cliente → día → producto**
- Muestra cantidades sin precios (solo producción, no valores financieros)
- En impresión: **una página por cliente** (usando `page-break-after`)
- Formato carta, print-friendly

**Entradas:**
- `GET /reportes/produccion-carne?semana=2024-01-02` (fecha = martes de la semana)

**Procesamiento:**
- Busca todas las OC activas de CARNES para la semana dada
- Para cada OC, extrae los detalles agrupados por día
- Por cada día, lista los productos con sus cantidades
- Calcula total por día y total general por cliente

**Salidas:**
```json
{
  "semanaInicio": "2024-01-02",
  "semanaFin": "2024-01-08",
  "clientes": [
    {
      "clienteNombre": "H.G.Z. No. 1",
      "totalGeneral": 150.5,
      "dias": [
        {
          "dia": "martes",
          "fecha": "2024-01-02",
          "productos": [
            { "productoNombre": "Pechuga de pollo", "cantidad": 20, "unidadMedida": "KILO" }
          ],
          "totalDia": 50.0
        }
      ]
    }
  ]
}
```

---

### 4.9 Usuarios / Configuración

**Propósito:** Administración de usuarios con matriz de permisos granular.

**Reglas de negocio:**
- Solo accesible por usuarios con rol **ADMIN**
- Panel izquierdo: lista de usuarios + toggle activo/inactivo
- Panel derecho: formulario de registro/edición + matriz de permisos (checkboxes módulo × acción)
- Un usuario inactivo no puede iniciar sesión
- Los permisos se asignan por usuario (tabla `usuario_permisos`)
- Al guardar permisos, se reemplazan todos los existentes (DELETE + INSERT)
- 3 roles pre-cargados:
  - **admin** (admin123): ADMIN con todos los permisos
  - **usuario** (user123): USER con permisos limitados
  - **visita** (pass1234): VIEWER solo lectura

**Campos de usuario:**
- `username`, `password` (BCrypt), `role` (ADMIN/USER/VIEWER)
- Datos adicionales: `nombreCompleto`, `correo`, `numero`, `cargo`

**Módulos disponibles:** PRODUCTOS, CLIENTES, CONTRATOS, NOTAS_VENTA, ORDENES_COMPRA, REPORTES, USUARIOS
**Acciones disponibles:** CREAR, LEER, ACTUALIZAR, ELIMINAR

**Salidas:** Lista de usuarios con permisos, creación/actualización de usuarios.

---

### 4.10 Super Admin

**Propósito:** Panel oculto para administradores que permite borrado forzoso (soft-delete) de cualquier nota, orden o cancelación, **saltando todas las reglas de negocio**.

**Reglas de negocio:**
- Ruta: `/admin/super` (solo visible para ADMIN)
- Elimina por UUID o por folio
- No verifica estados (puede eliminar notas firmadas, OC facturadas, etc.)
- Es soft-delete (`activo = false`), no destruye datos
- Opera sobre: NotaVenta (por ID o folio), OrdenCompra (por ID), Cancelación (por ID)

**Entradas:**
- `DELETE /admin/super/nota/{id}`
- `DELETE /admin/super/nota/folio/{folio}`
- `DELETE /admin/super/orden/{id}`
- `DELETE /admin/super/cancelacion/{id}`

**Salidas:** `{ "mensaje": "Nota eliminada correctamente" }`

---

## 5\. Seguridad

### JWT

- **Algoritmo**: HMAC-SHA (jjwt 0.12.6)
- **Secreto**: `laveronica-siscontrol-jwt-secret-key-2024-muy-seguro-para-produccion`
- **Expiración**: 24 horas (86400000 ms)
- **Claims**: `sub` (username), `iat` (issued at), `exp` (expiration)

### SecurityFilterChain

| Ruta | Acceso |
|------|--------|
| `/auth/**` | Público |
| `/enums/**` | Público |
| `/h2-console/**` | Público |
| `/v3/api-docs/**`, `/swagger-ui/**` | Público |
| `/usuarios/**` | ADMIN |
| `/admin/**` | ADMIN |
| Cualquier otra | Autenticado |

No se usa `@PreAuthorize` ni `@Secured`. Toda la seguridad se define en el `SecurityFilterChain`.

### CORS

- Desarrollo: `http://localhost:4200` (Angular dev server)
- Producción: mismo origen (nginx proxy reverso)
- Métodos permitidos: GET, POST, PUT, PATCH, DELETE, OPTIONS
- Headers permitidos: todos
- Credentials: true

### Flujo de autenticación

1. Usuario envía `POST /auth/login` con username + password
2. Backend valida con `AuthenticationManager` y genera JWT
3. Frontend almacena el token en `localStorage`
4. Cada request incluye `Authorization: Bearer <token>`
5. `JwtAuthFilter` extrae y valida el token en cada request
6. Si el token es inválido o expiró, el filter continúa sin autenticar (no lanza excepción)
7. El interceptor HTTP del frontend captura 401 y redirige al login

---

## 6\. Frontend

### App Shell

```
┌──────────────────────────────────────────────────────────┐
│  Top Header (full-width)                                  │
│  [☰] Sistema Veronica | Control de facturación           │
│                    fecha y hora actual | [⋮]              │
│                                           Modo oscuro     │
│                                           Ajustes         │
│                                           Cerrar sesión   │
├───────────┬──────────────────────────────────────────────┤
│ Sidebar   │  Router Outlet                                │
│ (colaps.) │                                               │
│           │                                               │
│ Dashboard │                                               │
│ Productos │                                               │
│ Clientes  │                                               │
│ ...       │                                               │
│ ────────  │                                               │
│ S. Admin* │                                               │
│           │                                               │
│ [👤 user] │                                               │
│ [🚪]      │                                               │
└───────────┴──────────────────────────────────────────────┘
  * Solo visible para ADMIN
```

### Componentes principales

| Ruta | Componente | Propósito |
|------|------------|-----------|
| `/` | LoginComponent | Pantalla de inicio de sesión |
| `/dashboard` | DashboardComponent | Panel principal |
| `/productos` | ProductoListaComponent | Lista paginada de productos |
| `/productos/nuevo` | ProductoFormComponent | Crear/editar producto |
| `/clientes` | ClienteListaComponent | Lista de clientes |
| `/contratos` | ContratoListaComponent | Lista de contratos |
| `/notaventas` | NotaVentaListaComponent | Lista paginada con filtros |
| `/notaventas/nuevo` | NotaVentaFormComponent | Crear/editar nota |
| `/notaventas/:id/ver` | NotaVentaDetalleComponent | Detalle + print |
| `/ordenes-compra` | OrdenListaComponent | Lista paginada con badges de estado |
| `/ordenes-compra/nuevo` | OrdenFormComponent | Crear OC (matriz productos×días) |
| `/ordenes-compra/:id/ver` | OrdenDetalleComponent | Detalle completo con notas, cancelaciones, extras |
| `/config` | ConfigComponent | Gestión de usuarios + permisos |
| `/admin/super` | SuperAdminComponent | Panel de borrado forzoso |
| `/reportes/produccion` | ReporteProduccionComponent | Reporte semanal |

### Servicios (16)

| Servicio | Endpoint base |
|----------|---------------|
| `AuthService` | `/auth` |
| `ProductoService` | `/productos` |
| `ProductoExcelService` | `/productos/excel` |
| `CategoriaService` | `/categorias` |
| `ClienteService` | `/clientes` |
| `ContratoService` | `/contratos` |
| `NotaVentaService` | `/notaventas` |
| `OrdenCompraService` | `/orden_compra` |
| `CancelacionService` | `/cancelaciones` |
| `ExtraService` | `/extras` |
| `FacturaService` | `/facturas` |
| `EnumsService` | `/enums` |
| `AdminService` | `/admin` |
| `UsuarioAdminService` | `/usuarios` |
| `ReporteProduccionService` | `/reportes` |

### Guards

| Guard | Función |
|-------|---------|
| `AuthGuard` | Protege todas las rutas excepto `/`. Redirige al login si no autenticado. |
| `AdminGuard` | Protege `/config` y `/admin/super`. Redirige al login si no es ADMIN. |

### Interceptor

`jwtInterceptor` (functional interceptor, Angular 17+): agrega el header `Authorization: Bearer <token>` a cada request. Captura errores 401 y redirige al login.

### Tema

- **Dark mode**: toggle desde el menú del header, persistido en `localStorage`
- **Sidebar colapsable**: toggle desde botón hamburguesa, persistido en `localStorage`
- **Reloj en vivo**: fecha y hora actualizadas cada segundo vía `setInterval` con `NgZone.runOutsideAngular`
- **Material Design**: Angular Material components con paleta personalizada vía SCSS

---

## 7\. Reglas de Negocio Específicas

### Semana laboral

| Día | Offset desde `fechaInicioSemana` |
|-----|----------------------------------|
| Martes | +0 |
| Miércoles | +1 |
| Jueves | +2 |
| Viernes | +3 |
| Sábado | +4 |
| Domingo | +5 |
| Lunes | +6 |

### Folios

| Entidad | Alcance | Método de cálculo |
|---------|---------|-------------------|
| NotaVenta | Global (toda la tabla) | `COALESCE(MAX(folio), 0) + 1` |
| Extra | Global | `COALESCE(MAX(folio), 0) + 1` |
| Factura | Global | `COALESCE(MAX(folio), 0) + 1` |

### Soft-delete

Todas las entidades tienen `activo BOOLEAN NOT NULL DEFAULT TRUE`. Ninguna operación elimina físicamente registros. Todas las consultas deben filtrar por `activo = true`.

### Estados de OrdenCompra

```
[PENDIENTE] → confirmar → [CONFIRMADA]
                                         ↘ generar notas → [NOTAS_GENERADAS]
                                                             ↘ firmar notas
                                                             ↘ validar cancelaciones
                                                             → [LISTO PARA FACTURAR]
                                                             → facturar → [FACTURADO] (bloqueado)
```

### Precios

- Los precios de venta se toman del producto al momento de crear la nota
- Los precios de compra son informativos (se usan en reportes)
- No hay historial de precios (se usa el precio actual del producto)

### NotaVenta — regla de fecha vs now()

La fecha de una NotaVenta (y Extra) generada desde una OC **nunca** usa `LocalDateTime.now()`. Siempre se calcula como:

```
fecha = orden.fechaInicioSemana.plusDays(offset(dia))
```

Esto garantiza que aunque la nota se genere días después, la fecha refleje el día real de la operación.

### Print — tamaño dinámico

| Condición | Tamaño de papel |
|-----------|-----------------|
| ≤8 productos | 1/4 carta (108×140mm) |
| >8 productos | 1/3 carta (108×186mm) |

### Extra — carga de productos

El diálogo de creación de extras carga **todos los productos de la partida** (sin paginación, page size 50), no solo los productos de la OC. El usuario selecciona el día en el diálogo y luego agrega productos con cantidades.

### Factura de Extras — precios

Los precios para la Factura de Extras se toman de las NotaVenta de la misma OC (no del precio actual del producto en catálogo). Si hay múltiples notas con diferentes precios para el mismo producto, se usa el precio de la primera nota encontrada.

---

## 8\. Despliegue

### Docker

**Backend Dockerfile** (multi-stage):
- Build stage: Amazon Corretto 17, Maven wrapper, compile + package
- Runtime stage: Amazon Corretto 17 JRE slim, copia JAR
- Expone puerto 8080

**Frontend Dockerfile** (multi-stage):
- Build stage: node:20-alpine, npm install + npm run build
- Runtime stage: nginx:stable-alpine, copia `dist/frontend/browser` y `nginx.conf`
- Expone puerto 80
- `docker-compose.yml`: servicios mysql + backend + frontend en red interna

### nginx.conf

```nginx
resolver 127.0.0.11 valid=10s;
server {
    listen 80;
    root /usr/share/nginx/html;

    location ~ ^/(auth|productos|...|admin)(/|$) {
        set $backend_upstream "http://sisveronica-backend:8080";
        proxy_pass $backend_upstream;
    }
    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

- Las rutas de API se proxy al backend mediante el nombre del servicio Docker (`sisveronica-backend:8080`)
- Usa `resolver 127.0.0.11` para resolución DNS dinámica de contenedores
- Las rutas no-API se sirven como SPA (`index.html` fallback)

### Variables de entorno

```
DB_SVERO_URL=mysql://host:3306/sis_veronica
DB_USER_NAME=root
DB_PASS=<contraseña>
```

Se pasan en el stack de Portainer, no en los Dockerfiles.

### OCI Ampere (ARM64)

- Las imágenes se construyen con `docker buildx build --platform linux/arm64`
- Se usa Amazon Corretto ARM64 para compatibilidad nativa
- Se forza la plataforma ARM64 en el build

---

## 9\. Convenciones de Código

### Backend

- Paquetes: `com.laveronica.siscontrol.{domain,services,controller,infra,enums}`
- DTOs: records inmutables en subpaquete `dto` dentro de cada dominio
- Repositorios: interfaces Spring Data con nombre `*Repository`
- Servicios: clases con anotación `@Service`, lógica de negocio pura
- Controladores: clases con `@RestController`, delegan a servicios
- Validaciones: `@Valid` en controllers, helpers de validación en servicios
- Sin comentarios en código

### Frontend

- Standalone components (sin NgModules)
- SCSS para estilos con soporte dark mode
- Modelos TypeScript en carpeta `models/`
- Servicios en carpeta `services/`
- Guards e interceptors funcionales
- Componentes agrupados por módulo en `components/{modulo}/`
- Rutas definidas en `app.routes.ts`

### Base de datos

- Migraciones Flyway con nomenclatura `V{numero}__{descripcion}.sql`
- IDs UUID como VARCHAR(36)
- Enums almacenados como VARCHAR
- Soft-delete con columna `activo`
- FKs con CONSTRAINTs explícitas
- Seed data con UUIDs legibles (prefijos por entidad)
