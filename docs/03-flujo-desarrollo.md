# Flujo de Desarrollo — SisVeronica

> Última actualización: 28/06/2026

---

## 1. Entorno Local

### 1.1 Prerrequisitos

| Herramienta | Versión | Propósito |
|-------------|---------|-----------|
| JDK | 17+ (Amazon Corretto recomendado) | Compilación y ejecución del backend |
| Node.js | 20+ | Compilación y desarrollo del frontend |
| npm | 11+ | Gestión de dependencias frontend |
| MySQL | 8+ | Base de datos |
| Docker Desktop | Última | Build de imágenes ARM64 |
| Git | Última | Control de versiones |

### 1.2 Clonar repositorio

```bash
git clone <repo-url> SisVeronica
cd SisVeronica
```

### 1.3 Configurar variables de entorno

El backend usa tres variables de entorno para la conexión a base de datos. En desarrollo local, puedes definirlas en tu perfil de PowerShell o sistema:

```powershell
# PowerShell
$env:DB_SVERO_URL = "mysql://localhost:3306/sis_veronica"
$env:DB_USER_NAME = "root"
$env:DB_PASS = "Admin.1516"
```

O crea un archivo `application-local.yml` en `src/main/resources/` (no versionado):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sis_veronica?createDatabaseIfNotExist=true
    username: root
    password: Admin.1516
```

### 1.4 Base de datos

```sql
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS sis_veronica;
```

Flyway crea automáticamente todas las tablas al iniciar el backend. No necesitas ejecutar migraciones manualmente.

### 1.5 Ejecutar backend

```bash
# Desde la raíz del proyecto
cd src
mvnw spring-boot:run
```

El backend inicia en `http://localhost:8080`. Swagger UI disponible en `http://localhost:8080/swagger-ui.html`.

Si usas IntelliJ IDEA, abre el proyecto y ejecuta `SisVeronicaApplication.java`.

### 1.6 Ejecutar frontend

```bash
cd frontend
npm install
npm start
```

El frontend inicia en `http://localhost:4200`. Se conecta al backend en `http://localhost:8080` (configurado en `environment.ts`).

### 1.7 Usuarios pre-cargados

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| `admin` | `admin123` | ADMIN (todos los permisos) |
| `usuario` | `user123` | USER (permisos limitados) |
| `visita` | `pass1234` | VIEWER (solo lectura) |

---

## 2. Estructura del Proyecto

```
SisVeronica/
├── docs/                    # Documentación
│   ├── index.md
│   ├── 01-documentacion-tecnica.md
│   ├── 02-contrato-rest.md
│   └── 03-flujo-desarrollo.md
├── src/                     # Backend Spring Boot
│   ├── main/
│   │   ├── java/com/laveronica/siscontrol/
│   │   │   ├── SisVeronicaApplication.java
│   │   │   ├── controller/          # 15 controladores REST
│   │   │   ├── services/            # 15 servicios
│   │   │   ├── domain/
│   │   │   │   ├── categoria/       # + dto/
│   │   │   │   ├── clientes/        # + dto/
│   │   │   │   ├── contratos/       # + dto/
│   │   │   │   ├── productos/       # + dto/
│   │   │   │   ├── ordencompra/     # + dto/
│   │   │   │   ├── ordencompradetalle/ + dto/
│   │   │   │   ├── notaventa/       # + dto/
│   │   │   │   ├── notaventadetalle/ + dto/
│   │   │   │   ├── notacancelacion/ # + dto/
│   │   │   │   ├── notacancelaciondetalle/ + dto/
│   │   │   │   ├── factura/         # + dto/
│   │   │   │   ├── facturadetalle/  # + dto/
│   │   │   │   ├── extra/           # + dto/
│   │   │   │   ├── extradetalle/    # + dto/
│   │   │   │   └── usuario/         # + dto/
│   │   │   ├── infra/security/      # JWT, SecurityConfig
│   │   │   ├── enums/               # Partida, Role, UnidadMedida, Modulo, Accion
│   │   │   └── helpers/validaciones/ # ValidacionesHelper
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/        # V1__.sql ... V17__.sql
│   ├── test/
│   │   └── java/.../                # Tests unitarios
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                 # Angular SPA
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/         # Componentes standalone
│   │   │   │   ├── auth/
│   │   │   │   ├── layout/
│   │   │   │   ├── productos/
│   │   │   │   ├── clientes/
│   │   │   │   ├── contratos/
│   │   │   │   ├── notaventas/
│   │   │   │   ├── ordenes-compra/
│   │   │   │   ├── config/
│   │   │   │   ├── super-admin/
│   │   │   │   └── reportes/
│   │   │   ├── models/             # Interfaces TypeScript
│   │   │   └── services/           # Servicios HTTP + guards + interceptor
│   │   ├── environments/           # environment.ts, environment.prod.ts
│   │   ├── index.html
│   │   ├── main.ts
│   │   └── styles.scss
│   ├── angular.json
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
├── docker-compose.yml
└── .gitignore
```

---

## 3. Cómo agregar una entidad nueva

### Paso 1: Migration SQL

Crear archivo `src/main/resources/db/migration/V{numero}__{descripcion}.sql`:

```sql
CREATE TABLE nueva_entidad (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Seed data (opcional)
INSERT INTO nueva_entidad (id, nombre) VALUES
('n1b20001-...', 'Valor 1');
```

**Convenciones:**
- Usar prefijo de UUID legible (ej. `n1` para nueva_entidad)
- Incluir CONSTRAINTs de FK donde aplique
- Incluir columna `activo` para soft-delete

### Paso 2: Entity + DTOs

Crear entity en `domain/nuevaentidad/NuevaEntidad.java`:

```java
@Table(name = "nueva_entidad") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(of = "id")
public class NuevaEntidad {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @NotNull private String nombre;
    @Column(nullable = false) private Boolean activo;
}
```

Crear DTOs en `domain/nuevaentidad/dto/`:
- `DatosRegistroNuevaEntidad.java` (record)
- `DatosListarNuevaEntidad.java` (record con constructor desde entity)
- `DatosDetalleNuevaEntidad.java` (record con constructor desde entity)

### Paso 3: Repository

```java
public interface NuevaEntidadRepository extends JpaRepository<NuevaEntidad, String> {
    Optional<NuevaEntidad> findByIdAndActivoTrue(String id);
}
```

### Paso 4: Service

```java
@Service @RequiredArgsConstructor
public class NuevaEntidadService {
    private final NuevaEntidadRepository repository;

    public DatosDetalleNuevaEntidad crear(DatosRegistroNuevaEntidad datos) {
        // Validaciones
        // Mapeo
        // Persistencia
        // Retorno DTO
    }

    // CRUD completo + lógica de negocio
}
```

### Paso 5: Controller

```java
@RestController @RequestMapping("/nueva-entidad") @RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NuevaEntidadController {
    private final NuevaEntidadService service;

    @PostMapping
    public ResponseEntity<DatosDetalleNuevaEntidad> crear(
            @Valid @RequestBody DatosRegistroNuevaEntidad datos,
            UriComponentsBuilder uriBuilder) { ... }

    @GetMapping
    public ResponseEntity<List<DatosListarNuevaEntidad>> listar() { ... }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleNuevaEntidad> buscar(@PathVariable String id) { ... }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) { ... }
}
```

### Paso 6: Frontend

1. Modelo en `frontend/src/app/models/nueva-entidad.model.ts`
2. Servicio en `frontend/src/app/services/nueva-entidad.service.ts`
3. Componente(s) en `frontend/src/app/components/nueva-entidad/`
4. Ruta en `app.routes.ts`
5. Item en menú en `app.ts`

### Convenciones

- **Entity**: `@AllArgsConstructor`, `@NoArgsConstructor`, `@Builder`, UUID, soft-delete
- **DTOs**: records inmutables, `@Valid` para validación
- **Repository**: Spring Data, consultas personalizadas con `@Query`
- **Service**: anotado `@Service`, `@Transactional`, lógica de negocio pura
- **Controller**: `@RestController`, delega a service, sin lógica de negocio
- **Frontend**: standalone components, sin NgModules
- **Sin comentarios en código**: el código debe ser auto-documentado

---

## 4. Docker Build

### 4.1 Backend

```dockerfile
# Dockerfile (backend)
FROM amazoncorretto:17 AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

FROM amazoncorretto:17-alpine AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build:
```powershell
docker buildx build --no-cache --platform linux/arm64 `
  -t chuycode/sistemveronica-backend:latest --push .
```

### 4.2 Frontend

```dockerfile
# Dockerfile (frontend)
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

Build:
```powershell
docker buildx build --no-cache --platform linux/arm64 `
  -t chuycode/sistemveronica-frontend:latest --push .
```

### 4.3 Multi-stage builds

Ambos Dockerfiles usan multi-stage:
- **Stage 1 (build)**: compila la aplicación
- **Stage 2 (runtime)**: imagen mínima solo con lo necesario para ejecutar

### 4.4 Variables de entorno en runtime

Las variables de entorno se pasan en tiempo de ejecución (Portainer stack), no en build:

```yaml
# docker-compose.yml
services:
  backend:
    image: chuycode/sistemveronica-backend:latest
    environment:
      DB_SVERO_URL: mysql://mysql:3306/sis_veronica
      DB_USER_NAME: root
      DB_PASS: ${DB_PASS}
```

---

## 5. OCI / Portainer

### 5.1 docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASS}
      MYSQL_DATABASE: sis_veronica
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    image: chuycode/sistemveronica-backend:latest
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      DB_SVERO_URL: mysql://mysql:3306/sis_veronica
      DB_USER_NAME: root
      DB_PASS: ${DB_PASS}
    ports:
      - "8080:8080"

  frontend:
    image: chuycode/sistemveronica-frontend:latest
    depends_on:
      - backend
    ports:
      - "80:80"

volumes:
  mysql_data:
```

### 5.2 Portainer Stack

1. En Portainer, crear nuevo stack
2. Pegar `docker-compose.yml`
3. Agregar variable de entorno `DB_PASS`
4. Desplegar

El stack crea tres servicios en red interna:
- `mysql` → `sisveronica-backend:8080` → `sisveronica-frontend:80`

### 5.3 nginx reverse proxy

El frontend sirve en el puerto 80. nginx hace proxy reverso al backend usando el nombre del servicio Docker:

```nginx
resolver 127.0.0.11 valid=10s;
location ~ ^/(auth|productos|...|admin)(/|$) {
    set $backend_upstream "http://sisveronica-backend:8080";
    proxy_pass $backend_upstream;
}
location / {
    try_files $uri $uri/ /index.html;
}
```

El `resolver 127.0.0.11` permite la resolución DNS dinámica de los contenedores Docker.

### 5.4 OCI Ampere ARM64

- Las imágenes se construyen con `docker buildx` forzando `--platform linux/arm64`
- Se usa Amazon Corretto ARM64 para compatibilidad nativa
- No se necesita emulación (Ampere es ARM64 nativo)

---

## 6. Testing

### 6.1 Backend

```bash
cd src
mvn test
```

153+ tests con JUnit 5 + Mockito + H2 in-memory. Los tests cubren:
- Servicios (lógica de negocio)
- Repositorios (consultas personalizadas)
- Controladores (integración REST)
- Validaciones

### 6.2 Frontend

```bash
cd frontend
npm run build
```

El build de Angular verifica:
- Compilación TypeScript
- Templates HTML
- Resolución de módulos
- Presupuestos de tamaño (2MB inicial, 4kB por componente)

No hay tests unitarios de frontend configurados actualmente (Vitest está disponible como dependencia).

### 6.3 Prueba manual del flujo completo

```
1. Login como admin/admin123
2. Ir a Productos → verificar lista
3. Ir a Clientes → verificar lista
4. Ir a Contratos → verificar lista
5. Ir a Órdenes de Compra → crear nueva orden
   - Seleccionar cliente, contrato, partida CARNES
   - Agregar productos con cantidades por día
   - Guardar
6. Abrir detalle de la orden → confirmar
7. Generar todas las notas (7 notas, una por día)
8. Firmar algunas notas
9. Crear cancelación para un día → validar
10. Ir a la orden → verificar que notas reflejan la cancelación
11. Generar factura
12. Ir a extras → crear extra → firmar
13. Generar factura de extras
14. Ir a Reporte Producción → seleccionar semana → ver reporte
15. Ir a Config → ver/editar usuarios
16. (Admin) Ir a Super Admin → eliminar una nota de prueba
```

---

## 7. Convenciones de Código

### Nombrado

| Elemento | Convención | Ejemplo |
|----------|------------|---------|
| Paquetes | minúsculas, separado por puntos | `com.laveronica.siscontrol.domain.cliente` |
| Clases | PascalCase | `ClienteService`, `DatosRegistroCliente` |
| Métodos | camelCase | `registrarCliente()`, `buscarPorId()` |
| Variables | camelCase | `datosCliente`, `fechaInicioSemana` |
| Constantes | UPPER_SNAKE_CASE | `ROLE_ADMIN` |
| Tablas DB | snake_case | `nota_ventas`, `orden_compras` |
| Columnas DB | snake_case | `fecha_inicio_semana`, `total_general` |
| Migraciones Flyway | V{numero}__{desc}.sql | `V18__nueva_entidad.sql` |

### Estructura de paquetes (backend)

```
domain/
└── nuevaentidad/
    ├── NuevaEntidad.java              # Entity
    ├── dto/
    │   ├── DatosRegistroNuevaEntidad.java  # Record
    │   ├── DatosListarNuevaEntidad.java    # Record
    │   └── DatosDetalleNuevaEntidad.java   # Record
    └── (opcional) mapper/
        └── NuevaEntidadMapper.java
```

### Estructura de archivos (frontend)

```
components/
└── nueva-entidad/
    ├── nueva-entidad-lista.component.ts
    ├── nueva-entidad-lista.component.html
    ├── nueva-entidad-form.component.ts
    └── nueva-entidad-form.component.html
```

### Principios

- **Clean Code**: nombres descriptivos, métodos pequeños, single responsibility
- **Sin comentarios**: el código debe ser auto-explicativo
- **DTOs inmutables**: usar records de Java (no mutables)
- **Validación temprana**: validar en el controller con `@Valid`, no en service
- **Transaccionalidad**: `@Transactional` en servicios que modifican datos
- **Soft-delete siempre**: nunca eliminar físicamente, solo `activo = false`
- **UUIDs**: usar `GenerationType.UUID` en Hibernate, no auto-increment
- **Enums como VARCHAR**: almacenar enums como strings en BD (legible en dumps)

### Git

- Commits descriptivos en español/inglés
- No commitear archivos generados (`node_modules/`, `target/`, `dist/`)
- No commitear secretos (claves, tokens)
- Las migraciones Flyway no deben modificarse después de commit

---

## 8. Troubleshooting

### Problema: Flyway checksum mismatch

```bash
# Drop y recreate la BD (solo en desarrollo)
DROP DATABASE sis_veronica;
CREATE DATABASE sis_veronica;
```

O forzar reparación (no recomendado en producción):
```bash
# En consola MySQL
DELETE FROM flyway_schema_history;
```

### Problema: CORS bloqueado

Verificar que `CorsConfigurationSource` permite `http://localhost:4200`.

### Problema: JWT expirado

El token expira a las 24 horas. Hacer login nuevamente.

### Problema: Puerto en uso

```bash
# Verificar que ocupa el puerto 8080
netstat -ano | findstr :8080

# Encontrar el proceso
tasklist | findstr <PID>
```

### Problema: MySQL connection refused

Verificar:
- MySQL está corriendo (`net start MySQL80`)
- El puerto 3306 está abierto
- Las variables de entorno están configuradas correctamente
- El usuario tiene permisos de conexión remota (si aplica)

### Problema: npm install falla

```bash
# Limpiar caché y reinstalar
cd frontend
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Problema: docker buildx no disponible

```bash
# Crear el builder si no existe
docker buildx create --name mybuilder --use
docker buildx inspect --bootstrap
```
