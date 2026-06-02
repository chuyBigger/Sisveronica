# SisVeronica

Sistema de control de facturación y administración para la carnicería **"La Verónica"**.

Flujo operativo: **Órdenes de Compra** (semanales) → **Notas de Venta** (diarias, folio secuencial) → **Cancelaciones** → **Reportes**.

---

## Stack tecnológico

| Capa | Tecnología | Versión |
|------|-----------|---------|
| Backend | Java | 17 |
| Backend | Spring Boot | 3.5.4 |
| Backend | Spring Security + JWT | (jjwt) |
| Backend | Spring Data JPA | Hibernate 6 |
| Backend | Flyway | 11.7.2 |
| Backend | MySQL | 8.0+ |
| Backend | Lombok + MapStruct | Última |
| Backend | Maven | Wrapper incluido |
| Frontend | Angular | 21 |
| Frontend | Angular Material | UI |
| Frontend | TypeScript | — |

---

## Requisitos

- **Java 17+** (JDK)
- **MySQL 8.0+**
- **Node.js 18+** (para el frontend)
- **Maven** (usar `mvnw.cmd` incluido)

---

## Configuración

### Base de datos

```sql
CREATE DATABASE sis_veronica;
```

Credenciales en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sis_veronica
spring.datasource.username=root
spring.datasource.password=Admin.1516
```

### Ejecutar backend

```bash
.\mvnw.cmd clean spring-boot:run
```

Flyway ejecuta las 18 migraciones automáticamente (V1–V20).

### Ejecutar frontend

```bash
cd frontend
npm install
ng serve
```

Frontend en `http://localhost:4200`, Backend en `http://localhost:8080`.

---

## Credenciales de acceso

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | ADMIN |
| `usuario` | `user123` | USER |
| `visita` | `pass1234` | VIEWER |

---

## Funciones implementadas

### 1. Autenticación y Seguridad

- Login con JWT (token Bearer)
- Roles: **ADMIN**, **USER**, **VIEWER**
- Permisos granulares por módulo × acción (CREAR, LEER, ACTUALIZAR, ELIMINAR)
- `AuthGuard` protege todas las rutas excepto login
- `AdminGuard` protege rutas de administración
- `jwtInterceptor` agrega token automáticamente y redirige a login en 401
- Registro de usuarios nuevos (solo admin)
- Logout con limpieza de token

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/auth/login` | Login |
| POST | `/auth/register` | Registro de usuario |

---

### 2. Clientes

- CRUD completo (Crear, Leer, Actualizar, Eliminar)
- Baja lógica (campo `activo`)
- Búsqueda por nombre, RFC o municipio

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/clientes` | Crear cliente |
| GET | `/clientes` | Listar clientes |
| GET | `/clientes/{id}` | Buscar por UUID |
| PATCH | `/clientes/{id}` | Actualizar cliente |
| DELETE | `/clientes/{id}` | Eliminar (baja lógica) |

---

### 3. Contratos

- CRUD completo
- Vinculados a un cliente
- Fechas de inicio y término
- Presupuesto asignado

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/contratos` | Crear contrato |
| GET | `/contratos` | Listar contratos |
| GET | `/contratos/{id}` | Buscar por UUID |
| PATCH | `/contratos/{id}` | Actualizar contrato |
| DELETE | `/contratos/{id}` | Eliminar (baja lógica) |

---

### 4. Categorías

- CRUD completo
- Asociadas a una partida

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/categorias` | Crear categoría |
| GET | `/categorias` | Listar categorías |
| GET | `/categorias/{id}` | Buscar por UUID |
| PATCH | `/categorias/{id}` | Actualizar categoría |
| DELETE | `/categorias/{id}` | Eliminar (baja lógica) |

---

### 5. Productos

- CRUD completo con popup de preview/edición inline
- Filtrado por partida
- Búsqueda por palabra clave
- Paginación
- 25 productos oficiales de carnes pre-cargados (V12)

**Popup de producto:**
- **Modo vista**: muestra nombre, código, partida, categoría, precio de venta
- **Modo edición**: formulario inline con todos los campos editables (nombre, partida, categoría, código, unidad de medida, precio compra, precio venta)
- Botones: Editar, Borrar, Guardar, Cancelar, Cerrar

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/productos` | Crear producto |
| GET | `/productos` | Listar productos (paginado) |
| GET | `/productos/{id}` | Buscar por UUID |
| GET | `/productos/partidas/{partida}` | Filtrar por partida |
| GET | `/productos/categorias/{id}` | Filtrar por categoría |
| GET | `/productos/buscar/{nombre}` | Buscar por nombre exacto |
| GET | `/productos/buscar_palabras?q=` | Búsqueda por palabra |
| PATCH | `/productos/{id}` | Actualizar producto |
| DELETE | `/productos/{id}` | Eliminar (baja lógica) |

---

### 6. Órdenes de Compra

- CRUD completo
- **Vista de detalle** (`/ordenes-compra/:id/ver`):
  - Toolbar: Volver, Editar, Eliminar, Confirmar, Crear Notas
  - Tabla producto × día (Lunes a Domingo) con totales
  - Badge de confirmación
  - Sección de cancelaciones
  - Mini-cards de notas de venta asociadas
- **Formulario de creación**: selección cliente → contrato → partida → tabla editable producto×día
- **Confirmación**: registro de quién confirmó y fecha
- **Unicidad**: validación de (cliente + partida + semana) para duplicados
- **Generación de notas**: generar todas las notas de venta desde la orden
- **Partida GENERAL**: carga todos los productos (no filtrados por partida)

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/orden_compra` | Crear orden |
| GET | `/orden_compra` | Listar órdenes (paginado) |
| GET | `/orden_compra/{id}` | Buscar por UUID |
| PATCH | `/orden_compra/{id}` | Actualizar orden |
| DELETE | `/orden_compra/{id}` | Eliminar orden |
| POST | `/orden_compra/{id}/confirmar` | Confirmar orden |
| GET | `/orden_compra/{id}/notas` | Listar notas de la orden |
| POST | `/orden_compra/{id}/generar-notas` | Generar todas las notas |

---

### 7. Notas de Venta

- CRUD completo con popup de formulario y preview
- **Folio secuencial automático**: `COALESCE(MAX(folio), 0) + 1`
- **Generación desde orden**: crear nota individual desde una orden de compra por día
- **Preview**: vista previa estilo remisión con logo, datos fiscales, tabla de productos, total
- **Impresión**: formato carta (21.59cm × 13.97cm)
- **Detalle completo** (`/notaventas/:id/ver`): vista read-only con menú lateral
- **Indicador visual**: borde naranja en notas que tienen cancelaciones asociadas
- **Campo `dia`**: día de la semana asociado a la nota

**Popup de nota de venta:**
- Menú lateral con: Editar, Imprimir, Borrar, Cerrar
- Vista previa estilo remisión fiscal

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/notaventas` | Crear nota |
| POST | `/notaventas/generar-desde-orden` | Generar desde orden |
| GET | `/notaventas` | Listar notas (paginado) |
| GET | `/notaventas/{id}` | Buscar por UUID |
| PATCH | `/notaventas/{id}` | Actualizar nota |
| DELETE | `/notaventas/{id}` | Eliminar (baja lógica) |

---

### 8. Cancelaciones (Notas de Cancelación)

- Crear cancelación por orden de compra + día
- **Selección de día**: se elige el día de la semana a cancelar
- **Ajuste por producto**: cantidad a cancelar (validada contra cantidades de la OC)
- **Validación**: aprobar cancelación (registra quién validó y fecha)
- **Reconstrucción de notas**: recalcula las Notas de Venta afectadas restando las cancelaciones validadas
- **Indicador visual**: borde rojo (pendiente) / verde (validada) en cada cancelación

**Popup de cancelación:**
- Selección del día
- Lista de productos con cantidades editables
- Botones: Guardar, Cerrar

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/cancelaciones` | Crear cancelación |
| GET | `/cancelaciones/orden/{ordenCompraId}` | Listar por orden |
| POST | `/cancelaciones/{id}/validar` | Validar cancelación |
| DELETE | `/cancelaciones/{id}` | Eliminar cancelación |
| POST | `/cancelaciones/reconstruir/{ordenCompraId}` | Reconstruir notas |

---

### 9. Administración de Usuarios

- CRUD de usuarios (solo ADMIN)
- Asignación de permisos granulares por módulo × acción
- Habilitar/deshabilitar usuarios
- Panel de configuración con tabs: Usuarios / Permisos

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/usuarios` | Listar usuarios |
| GET | `/usuarios/{id}` | Detalle de usuario |
| POST | `/usuarios` | Crear usuario |
| PUT | `/usuarios/{id}/permisos` | Asignar permisos |
| PATCH | `/usuarios/{id}/toggle` | Habilitar/deshabilitar |

---

### 10. Enums

**Endpoints:**

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/enums/partidas` | Listar partidas |
| GET | `/enums/unidades-medida` | Listar unidades de medida |

**Valores disponibles:**

| Enum | Valores |
|------|---------|
| Partida | ABARROTES, CARNES, LACTEOS, FRUTASYVERDURAS, VARIOS, GENERAL |
| UnidadMedida | KILO, LITRO, PIEZA, PAQUETE |
| Role | ADMIN, USER, VIEWER |
| Modulo | PRODUCTOS, CLIENTES, CONTRATOS, NOTAS_VENTA, ORDENES_COMPRA, REPORTES, USUARIOS |
| Accion | CREAR, LEER, ACTUALIZAR, ELIMINAR |
| DiaSemana | LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO |

---

## Modelo de datos

Todas las entidades usan **UUID** como identificador primario (`VARCHAR(36)`) generado automáticamente por Hibernate.

| Entidad | Tabla | Campos clave |
|---------|-------|-------------|
| Cliente | `clientes` | nombre, rfc (único), dirección, municipio, estado |
| Contrato | `contratos` | contrato (único), cliente FK, fechas, presupuesto |
| Categoria | `categorias` | nombre, partida |
| Producto | `productos` | código (único), nombre (único), partida, categoría FK, unidadMedida, precios |
| OrdenCompra | `orden_compras` | cliente FK, contrato FK, partida, semana, confirmadoPor |
| OrdenCompraDetalle | `orden_compra_detalles` | orden FK, producto FK, lunes..domingo (cantidades) |
| NotaVenta | `nota_ventas` | folio (único), cliente FK, contrato FK, orden FK, fecha, partida, dia |
| NotaVentaDetalle | `nota_venta_detalles` | nota FK, producto FK, cantidad, precio, subTotal |
| NotaCancelacion | `nota_cancelaciones` | orden FK, dia, creadoPor, validadoPor |
| NotaCancelacionDetalle | `nota_cancelacion_detalles` | cancelacion FK, producto FK, cantidadCancelada |
| Usuario | `usuarios` | username (único), password (BCrypt), role |
| UsuarioPermiso | `usuario_permisos` | usuario FK, modulo, acción (único por usuario) |

---

## Migraciones de base de datos

| Migración | Descripción |
|-----------|-------------|
| V1 | Tabla `clientes` |
| V2 | Tabla `contratos` |
| V3 | Tabla `productos` |
| V4 | Tabla `categorias` |
| V5 | Tabla `nota_ventas` |
| V7 | Tabla `nota_venta_detalles` |
| V8 | Tabla `orden_compras` |
| V9 | Tabla `orden_compra_detalles` (con columnas LUNES–DOMINGO) |
| V10 | Agregar `producto_id` a `orden_compra_detalles` |
| V11 | Agregar `orden_compra_id` a `nota_ventas` |
| V12 | Seed data: categorías, clientes, contratos, órdenes, notas, 25 productos de carnes |
| V14 | Tabla `usuarios` + 3 usuarios pre-cargados |
| V15 | Tabla `usuario_permisos` + permisos pre-cargados |
| V16 | Cliente "General" para partidas GENERAL |
| V17 | Constraint UNIQUE en (cliente_id, partida, fecha_inicio_semana) |
| V18 | Columnas `confirmado_por`, `fecha_confirmacion` en `orden_compras` |
| V19 | Columna `dia` en `nota_ventas` |
| V20 | Tablas `nota_cancelaciones` y `nota_cancelacion_detalles` |

---

## Frontend - Rutas

| Ruta | Componente | Protegida |
|------|-----------|-----------|
| `/` | LoginComponent | No |
| `/dashboard` | DashboardComponent | AuthGuard |
| `/productos` | ProductoListaComponent | AuthGuard |
| `/productos/nuevo` | ProductoFormComponent | AuthGuard |
| `/productos/:id` | ProductoFormComponent | AuthGuard |
| `/clientes` | ClienteListaComponent | AuthGuard |
| `/clientes/nuevo` | ClienteFormComponent | AuthGuard |
| `/clientes/:id` | ClienteFormComponent | AuthGuard |
| `/contratos` | ContratoListaComponent | AuthGuard |
| `/contratos/nuevo` | ContratoFormComponent | AuthGuard |
| `/contratos/:id` | ContratoFormComponent | AuthGuard |
| `/notaventas` | NotaVentaListaComponent | AuthGuard |
| `/notaventas/nuevo` | NotaVentaFormComponent | AuthGuard |
| `/notaventas/:id` | NotaVentaFormComponent | AuthGuard |
| `/notaventas/:id/ver` | NotaVentaDetalleComponent | AuthGuard |
| `/ordenes-compra` | OrdenListaComponent | AuthGuard |
| `/ordenes-compra/nuevo` | OrdenFormComponent | AuthGuard |
| `/ordenes-compra/:id/ver` | OrdenDetalleComponent | AuthGuard |
| `/ordenes-compra/:id` | OrdenFormComponent | AuthGuard |
| `/config` | ConfigComponent | AuthGuard |

---

## Frontend - Funciones de UI

### Todas las vistas de lista
- **Columnas ordenables**: MatSort en todos los encabezados de columna
- **Barras de búsqueda**: filtro por texto en todas las listas
- **Filtros específicos**: dropdown de Partida en notas de venta, órdenes y productos
- **Paginación**: MatTablePaginator con opciones 5, 10, 25, 50
- **Filas clickeables**: cursor pointer + hover effect, clic en cualquier parte de la fila abre vista/detalle
- **Botones de acción**: Editar, Eliminar (con stopPropagation para no activar clic de fila)

### Header global
- Nombre del sistema ("Sistema Veronica") + subtítulo
- Fecha actual (centro)
- Reloj en vivo (derecha)
- Menú kebab: Modo oscuro, Ajustes, Cerrar sesión
- Botón hamburguesa para colapsar/expandir sidebar

### Sidebar colapsable
- **Expandido** (260px): icono + texto de cada módulo
- **Colapsado** (64px): solo iconos con tooltip al hover
- Estado persistido en `localStorage`
- Sección de usuario con nombre, rol y botón de logout

### Modo oscuro
- Toggle en menú kebab del header
- Persiste en `localStorage`
- Variables CSS globales para todo el sistema
- Compatibilidad con todos los componentes

---

## Estructura del proyecto

```
SisVeronica/
├── src/main/java/com/laveronica/siscontrol/
│   ├── controller/              # 10 controladores REST
│   │   ├── AuthController.java
│   │   ├── UsuarioAdminController.java
│   │   ├── ClienteController.java
│   │   ├── ContratosController.java
│   │   ├── CategoriaController.java
│   │   ├── ProductoController.java
│   │   ├── NotaVentaController.java
│   │   ├── OrdenCompraController.java
│   │   ├── NotaCancelacionController.java
│   │   └── EnumsController.java
│   ├── domain/                  # Entidades JPA + DTOs + Mappers
│   │   ├── clientes/
│   │   ├── contratos/
│   │   ├── categoria/
│   │   ├── productos/
│   │   ├── notaventa/
│   │   ├── notaventadetalle/
│   │   ├── ordencompra/
│   │   ├── ordencompradetalle/
│   │   ├── notacancelacion/
│   │   ├── notacancelaciondetalle/
│   │   └── usuario/
│   ├── enums/                   # Partida, UnidadMedida, Role, Modulo, Accion, DiaSemana
│   ├── infra/
│   │   ├── exceptions/          # Manejo global de errores
│   │   └── security/            # JWT, SecurityConfig, CORS
│   ├── repositories/            # Interfaces JPA
│   ├── services/                # Lógica de negocio
│   └── utils/helpers/           # Validaciones reutilizables
│
├── src/main/resources/db/migration/  # 18 archivos SQL (V1–V20)
│
├── frontend/src/app/
│   ├── app.routes.ts            # Rutas Angular
│   ├── components/
│   │   ├── auth/                # Login
│   │   ├── layout/              # Sidebar, Dashboard
│   │   ├── clientes/            # Lista + Formulario
│   │   ├── contratos/           # Lista + Formulario
│   │   ├── productos/           # Lista + Formulario + Preview Dialog
│   │   ├── notaventas/          # Lista + Form + FormDialog + PreviewDialog + Detalle
│   │   ├── ordenes-compra/      # Lista + Form + Detalle + CancelacionFormDialog
│   │   └── config/              # Administración de usuarios
│   ├── services/                # 10 servicios Angular + guards + interceptor
│   └── models/                  # 8 archivos de interfaces TypeScript
│
└── docs/                        # Documentación adicional
```

---

## Convenciones

- **IDs**: UUID `VARCHAR(36)` generado por Hibernate (`GenerationType.UUID`)
- **Bajas lógicas**: columna `activo` en todas las tablas (nunca se eliminan registros físicamente)
- **Enums**: almacenados como string en BD (`@Enumerated(EnumType.STRING)`)
- **Paginación**: `@PageableDefault` con tamaño 9 o 10
- **Seed data**: Flyway V12 inserta datos de prueba con UUIDs explícitos
- **Passwords**: encriptados con BCrypt
- **CORS**: configurado para `localhost:4200` con todos los métodos HTTP
- **CSS**: variables CSS globales para soporte de modo oscuro
