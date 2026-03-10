



ºnterno de Gestión y Control de Pedidos

**¡Bienvenido a SIGCOP!**  
Sistema backend desarrollado con **Java y Spring Boot** que gestiona clientes, contratos y pedidos internos, optimizando la operación de cada departamento y generando reportes de rendimiento y metas económicas.

---

## 📝 Descripción

Este proyecto implementa un backend modular para gestión de pedidos, contratos y clientes:

- API RESTful para gestión de **Clientes** y **Contratos**.
- Manejo de operaciones CRUD, actualización parcial (PATCH) y eliminación lógica.
- Validaciones de datos con **Jakarta Validation**.
- Persistencia con **Spring Data JPA** y **MySQL**.
- Manejo centralizado de excepciones y mensajes de error claros.
- DTOs para entrada y salida de datos, separando lógica de negocio y persistencia.
- Arquitectura modular lista para evolucionar hacia microservicios por partidas.

---

## 📌 Funcionalidades principales

### ✅ Gestión de Clientes
- Registrar nuevos clientes.
- Consultar clientes existentes.
- Actualizar información de clientes.
- Eliminar clientes (eliminación lógica).

### ✅ Gestión de Contratos
- Registrar contratos asociados a clientes.
- Consultar contratos activos.
- Actualización parcial de contratos con PATCH.
- Eliminación lógica de contratos.
- Validaciones de reglas de negocio básicas.

### ✅ Operaciones adicionales
- Manejo de errores con mensajes claros.
- DTOs para separar la capa de dominio y la presentación.
- Preparado para integrar **JWT** y seguridad en el futuro.
- Documentación de API con **Swagger** (pendiente de completar).

---

## 🛠️ Tecnologías utilizadas

- 💻 **Lenguaje:** Java 17
- ⚙️ **Framework:** Spring Boot
- 🗄️ **Persistencia:** JPA / Hibernate con **MySQL**
- 🏷️ **Manejo de datos:** DTOs y **Jackson**
- 🔧 **Validación:** Jakarta Validation (`@Valid`)
- 📝 **Migraciones de base de datos:** Flyway
- 🛠️ **Productividad:** Lombok (`@Getter`, `@Setter`, `@AllArgsConstructor`)
- 🌐 **API REST:** `ResponseEntity` y status codes
- 📊 **Documentación:** Swagger (pendiente)
- 🔒 **Seguridad:** JWT (pendiente)

---

## 🚀 Cómo ejecutar el proyecto

1. Clona el repositorio:

```bash
git clone <url-del-repositorio>
```

2. Configura la base de datos en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sigcop
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```

3. Instala dependencias:

```bash
mvn install
```

4. Ejecuta la aplicación:

```bash
mvn spring-boot:run
```

5. Accede a la API (pendiente de documentación Swagger):

```
http://localhost:8080
```



## 📄 Estado actual del proyecto

✅ Funcionalidades implementadas:
- CRUD de **Clientes**
- CRUD de **Contratos**
- Actualización parcial (PATCH)
- Eliminación lógica
- Manejo básico de errores

🛠️ Mejoras previstas:
- Integración de **JWT** y seguridad de endpoints.
- Documentación completa con **Swagger**.
- Implementación de microservicios por partidas.
- Dashboards y reportes automáticos de operaciones y métricas.

---

## 📄 Licencia

Proyecto bajo **licencia MIT**:

```text
MIT License
Copyright (c) 2025 Jesús Medina Casas
```

---




# SisControl - Sistema de Control Operativo La Verónica

**¡Bienvenido a SisControl!** 🚀

Backend desarrollado con **Java y Spring Boot** para la gestión centralizada de la operación de ventas, contratos, clientes, y logística diaria de *La Verónica*. Este sistema está diseñado para garantizar la integridad de las transacciones y automatizar el registro de pedidos y entregas.

---

## 🏗️ Arquitectura y Dominio

Este proyecto implementa una arquitectura **API RESTful modular** enfocada en el dominio de las transacciones de venta.

### Módulos Implementados Recientemente:

| Módulo | Descripción | Relación JPA |
| :--- | :--- | :--- |
| **Clientes** | Gestión de maestros de clientes. | `1:N` a Contratos y Notas de Venta |
| **Días** | Mapeo de días operativos y sus estados. | `1:N` a Notas de Venta |
| **Nota de Venta** | Transacción principal (cabecera de la venta). | `1:N` a NotaVentaDetalle |
| **Nota de Venta Detalle** | Ítems, cantidades y subtotales por producto. | `N:1` a Nota de Venta y Producto |
| **Contratos** | Reglas de negocio y términos asociados al cliente. | `N:1` a Clientes |

### Características Clave:
* **Integridad Transaccional:** Uso riguroso de **`@Transactional`** para asegurar que las Notas de Venta y sus Detalles se guarden o fallen como una sola unidad.
* **Persistencia Robusta:** Mapeo de relaciones complejo (`@OneToMany`, `@ManyToOne`, `Enum`) validado y optimizado.
* **Separación de Responsabilidades:** Utilización de DTOs en la capa de presentación y servicios (`@Service`) para la lógica de negocio.
* **Manejo de Errores:** Excepciones y validaciones (`Jakarta Validation`) centralizadas.

---

## 📌 Tecnologías Utilizadas

| Categoría | Tecnología | Versión / Anotación |
| :--- | :--- | :--- |
| **Lenguaje** | Java | Java 17 |
| **Framework** | Spring Boot | 3.x |
| **Persistencia** | JPA / Hibernate | MySQL |
| **Base de Datos** | Migraciones | **Flyway** (Esquema validado) |
| **Productividad** | Mapeo y POJOs | **Lombok** (`@Getter`, `@AllArgsConstructor`) |
| **Validación** | Reglas de DTO | Jakarta Validation (`@Valid`, `@NotNull`) |
| **Testing** | Pruebas Unitarias | JUnit 5 (Próximo) |

---

## 🚀 Cómo Ejecutar el Proyecto

### 1. Requisitos
Asegúrate de tener instalado: **JDK 17** y **MySQL Server** (o similar).

### 2. Configuración de Base de Datos
Actualiza el archivo `application.properties` con tus credenciales:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sisveronica
spring.datasource.username=root
spring.datasource.password=su_contraseña
# Flyway se encarga de las migraciones
spring.jpa.hibernate.ddl-auto=validate 
Nota: El valor ddl-auto=validate es la configuración más segura en entornos con Flyway, ya que valida que el esquema de la DB coincida con las entidades JPA.

3. Ejecución
Clona el repositorio: git clone <url-del-repositorio>

Navega al directorio del proyecto.

Ejecuta la aplicación:

Bash

mvn spring-boot:run
4. Endpoints Principales (Ejemplo)
La API estará disponible en http://localhost:8080.

Funcionalidad	Método	URL	Body (DTO)
Registrar Nota	POST	/notas	RegistroNotaVentaDTO
Registrar Cliente	POST	/clientes	RegistroClienteDTO

Exportar a Hojas de cálculo
🛣️ Estado y Desarrollo Futuro
Implementado y Validado:

Modelo de Dominio (NotaVenta, Detalle, Día, Producto).

Integridad de Mapeo (JPA y Flyway sincronizados 100%).

Controladores y DTOs para Clientes y Contratos.

Endpoint para registrar nueva Nota de Venta (POST /notas).

Próximos Pasos (La Locura):

Implementar la lógica completa en el NotaVentaService (cálculo de totales, validación de existencia de IDs, etc.).

Desarrollo de la funcionalidad de Órdenes de Compra (OC).

Integración de JWT para autenticación y seguridad de endpoints.

Documentación completa de la API con Swagger.

👨‍💻 Desarrollador
Jesús Medina Casas

💻 Desarrollador Java y Spring Boot

🔗 LinkedIn

🐙 GitHub

📜 Licencia
Este proyecto está bajo la Licencia MIT.