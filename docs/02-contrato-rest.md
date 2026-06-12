# Contrato REST API — SisVeronica

---

## Convenciones generales

- **Base URL (desarrollo)**: `http://localhost:8080`
- **Base URL (producción)**: mismo origen (proxy nginx)
- **Content-Type**: `application/json`
- **Autenticación**: `Authorization: Bearer <token>` (excepto endpoints públicos)
- **IDs**: UUIDs de 36 caracteres (`VARCHAR(36)`)
- **Fechas**: `yyyy-MM-dd` (LocalDate), `yyyy-MM-dd'T'HH:mm:ss` (LocalDateTime)
- **Enums**: strings en PascalCase (ej. `"CARNES"`, `"ADMIN"`, `"KILO"`)
- **Paginación**: parámetros `page` (0-based), `size`, `sort`
- **Errores**: HTTP status codes estándar con mensaje descriptivo

### Códigos de respuesta

| Código | Significado |
|--------|-------------|
| 200 OK | Operación exitosa con body |
| 201 Created | Recurso creado exitosamente |
| 204 No Content | Operación exitosa sin body |
| 400 Bad Request | Datos inválidos (validación fallida) |
| 401 Unauthorized | Token faltante, inválido o expirado |
| 404 Not Found | Recurso no encontrado |
| 409 Conflict | Recurso duplicado o regla de negocio violada |
| 500 Internal Server Error | Error inesperado |

---

## 1. Auth — `/auth` [PÚBLICO]

### 1.1 Login

```
POST /auth/login
```

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcxODQwMDAwMCwiZXhwIjoxNzE4NDg2NDAwfQ.signature",
  "username": "admin",
  "role": "ADMIN",
  "tipo": "Bearer"
}
```

**Response 400 (credenciales inválidas):**
```json
{
  "mensaje": "Credenciales inválidas"
}
```

### 1.2 Register

```
POST /auth/register
```

**Request:**
```json
{
  "username": "nuevo_usuario",
  "password": "password123",
  "role": "USER"
}
```

**Response 201:**
```json
{
  "token": "eyJhbGciOiJIUzM4NCJ9...",
  "username": "nuevo_usuario",
  "role": "USER",
  "tipo": "Bearer"
}
```

---

## 2. Enums — `/enums` [PÚBLICO]

### 2.1 Listar Partidas

```
GET /enums/partidas
```

**Response 200:**
```json
["CARNES", "ABARROTES", "LACTEOS", "FRUTASYVERDURAS", "VARIOS", "GENERAL"]
```

### 2.2 Listar Unidades de Medida

```
GET /enums/unidades-medida
```

**Response 200:**
```json
["KILO", "LITRO", "PIEZA", "PAQUETE"]
```

### 2.3 Listar Módulos

```
GET /enums/modulos
```

**Response 200:**
```json
["PRODUCTOS", "CLIENTES", "CONTRATOS", "NOTAS_VENTA", "ORDENES_COMPRA", "REPORTES", "USUARIOS"]
```

### 2.4 Listar Acciones

```
GET /enums/acciones
```

**Response 200:**
```json
["CREAR", "LEER", "ACTUALIZAR", "ELIMINAR"]
```

---

## 3. Categorías — `/categorias` [AUTH]

### 3.1 Registrar categoría

```
POST /categorias
```

**Request:**
```json
{
  "nombre": "Carnes Frías",
  "partida": "CARNES"
}
```

**Response 201:** Location header con URI del recurso creado.

### 3.2 Listar categorías

```
GET /categorias
```

**Response 200:**
```json
[
  {
    "id": "b1b20001-0000-0000-0000-000000000001",
    "nombre": "Lácteos",
    "partida": "LACTEOS"
  },
  {
    "id": "b1b20001-0000-0000-0000-000000000002",
    "nombre": "Carnes Rojas",
    "partida": "CARNES"
  }
]
```

### 3.3 Buscar por ID

```
GET /categorias/{id}
```

**Response 200:**
```json
{
  "id": "b1b20001-0000-0000-0000-000000000001",
  "nombre": "Lácteos",
  "partida": "LACTEOS"
}
```

**Response 404:** `EntityNotFoundException`

### 3.4 Actualizar

```
PATCH /categorias/{id}
```

**Request:**
```json
{
  "nombre": "Lácteos y Derivados",
  "partida": "LACTEOS"
}
```

**Response 200:** DTO actualizado.

### 3.5 Eliminar (soft-delete)

```
DELETE /categorias/{id}
```

**Response 204:** No content.

---

## 4. Clientes — `/clientes` [AUTH]

### 4.1 Registrar cliente

```
POST /clientes
```

**Request:**
```json
{
  "nombre": "H.G.Z. No. 1",
  "rfc": "HGE010101AAA",
  "calle": "Av. Principal",
  "numero": 123,
  "fraccionamiento": "Centro",
  "cp": "12345",
  "municipio": "Monterrey",
  "estado": "Nuevo León"
}
```

**Response 201:** Location header con URI.

### 4.2 Listar clientes

```
GET /clientes
```

**Response 200:**
```json
[
  {
    "id": "c1b20001-0000-0000-0000-000000000001",
    "nombre": "H.G.Z. No. 1",
    "rfc": "HGE010101AAA",
    "calle": "Av. Principal",
    "numero": 123,
    "fraccionamiento": "Centro",
    "cp": "12345",
    "municipio": "Monterrey",
    "estado": "Nuevo León"
  }
]
```

### 4.3 Buscar por ID

```
GET /clientes/{id}
```

**Response 200:** DTO del cliente.

**Response 404:** `ResourceNotFoundException`

### 4.4 Actualizar

```
PATCH /clientes/{id}
```

**Request:**
```json
{
  "nombre": "H.G.Z. No. 1 (Actualizado)",
  "calle": "Av. Reforma",
  "cp": "54321"
}
```

**Response 200:** DTO actualizado.

### 4.5 Eliminar (soft-delete)

```
DELETE /clientes/{id}
```

**Response 204:** No content.

---

## 5. Contratos — `/contratos` [AUTH]

### 5.1 Registrar contrato

```
POST /contratos
```

**Request:**
```json
{
  "contrato": "CONTRATO-IMSS-2024-001",
  "clienteId": "c1b20001-0000-0000-0000-000000000001",
  "fechaInicio": "2024-01-01",
  "fechaTermino": "2024-12-31",
  "presupuesto": 500000.00
}
```

**Response 201:** Location header.

### 5.2 Listar contratos

```
GET /contratos
```

**Response 200:**
```json
[
  {
    "id": "d1b20001-0000-0000-0000-000000000001",
    "contrato": "CONTRATO-IMSS-2024-001",
    "cliente": "H.G.Z. No. 1",
    "fechaInicio": "2024-01-01",
    "fechaTermino": "2024-12-31",
    "presupuesto": 500000.00
  }
]
```

### 5.3 Buscar por ID

```
GET /contratos/{id}
```

**Response 200:** DTO del contrato.

### 5.4 Actualizar

```
PATCH /contratos/{id}
```

**Request:**
```json
{
  "presupuesto": 550000.00,
  "fechaTermino": "2025-06-30"
}
```

**Response 200:** DTO actualizado.

### 5.5 Eliminar (soft-delete)

```
DELETE /contratos/{id}
```

**Response 204:** No content.

---

## 6. Productos — `/productos` [AUTH]

### 6.1 Registrar producto

```
POST /productos
```

**Request:**
```json
{
  "nombre": "Pechuga de Pollo",
  "partida": "CARNES",
  "categoriaId": "b1b20001-0000-0000-0000-000000000002",
  "unidadMedida": "KILO",
  "precioCompra": 45.00,
  "precioVenta": 89.00,
  "codigo": "CARNES-001"
}
```

**Response 201:** Location header.

### 6.2 Listar productos (paginado)

```
GET /productos?page=0&size=9&sort=nombre,asc
```

**Response 200:**
```json
{
  "content": [
    {
      "id": "a1b20001-0000-0000-0000-000000000001",
      "nombre": "Pechuga de Pollo",
      "partida": "CARNES",
      "categoria": "Carnes Rojas",
      "codigo": "CARNES-001",
      "precioVenta": 89.00
    }
  ],
  "totalElements": 25,
  "totalPages": 3,
  "size": 9,
  "number": 0
}
```

### 6.3 Listar por partida

```
GET /productos/partidas/CARNES?page=0&size=10
```

**Response 200:** Page de productos.

### 6.4 Listar por categoría

```
GET /productos/categorias/{categoriaId}?page=0&size=10
```

**Response 200:** Page de productos.

### 6.5 Buscar por ID

```
GET /productos/{id}
```

**Response 200:**
```json
{
  "id": "a1b20001-0000-0000-0000-000000000001",
  "nombre": "Pechuga de Pollo",
  "partida": "CARNES",
  "categoriaId": "b1b20001-0000-0000-0000-000000000002",
  "unidadMedida": "KILO",
  "codigo": "CARNES-001",
  "precioCompra": 45.00,
  "precioVenta": 89.00
}
```

### 6.6 Buscar por nombre exacto

```
GET /productos/buscar/Pechuga de Pollo
```

**Response 200:** DTO del producto.

### 6.7 Buscar por palabras clave

```
GET /productos/buscar_palabras?q=pollo&page=0&size=10
```

**Response 200:** Page de productos que contienen "pollo" en el nombre.

### 6.8 Actualizar

```
PATCH /productos/{id}
```

**Request:**
```json
{
  "precioVenta": 95.00,
  "precioCompra": 48.00
}
```

**Response 200:** DTO actualizado.

### 6.9 Eliminar (soft-delete)

```
DELETE /productos/{id}
```

**Response 200:** OK (sin body).

---

## 7. Productos Excel — `/productos/excel` [AUTH]

### 7.1 Cargar productos desde Excel

```
POST /productos/excel/cargar
Content-Type: multipart/form-data
```

**Request:** Archivo `.xlsx` en campo `archivo`.

**Response 200:**
```json
{
  "totalProcesados": 25,
  "exitosos": 23,
  "duplicados": 1,
  "sinPrecio": 1,
  "mensajesDuplicados": ["El producto 'Pechuga de Pollo' ya existe"],
  "mensajesSinPrecio": ["El producto 'Milanesa de Pollo' no tiene precio de venta"]
}
```

### 7.2 Descargar plantilla

```
GET /productos/excel/plantilla
```

**Response 200:** `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` (archivo `.xlsx`).

---

## 8. Órdenes de Compra — `/orden_compra` [AUTH]

### 8.1 Registrar orden

```
POST /orden_compra
```

**Request:**
```json
{
  "cliente_id": "c1b20001-0000-0000-0000-000000000001",
  "contrato_id": "d1b20001-0000-0000-0000-000000000001",
  "partida": "CARNES",
  "fechaInicioSemana": "2024-01-02",
  "detalles": [
    {
      "fecha": "2024-01-02",
      "producto": "a1b20001-0000-0000-0000-000000000001",
      "martes": 10,
      "miercoles": 8,
      "jueves": 12,
      "viernes": 10,
      "sabado": 6,
      "domingo": 4,
      "lunes": 5
    }
  ]
}
```

**Response 201:**
```json
{
  "id": "e1b20001-0000-0000-0000-000000000001",
  "cliente": "H.G.Z. No. 1",
  "contrato": "CONTRATO-IMSS-2024-001",
  "partida": "CARNES",
  "fechaInicioSemana": "2024-01-02",
  "detalles": [
    {
      "id": "e2b20001-0000-0000-0000-000000000001",
      "producto": "a1b20001-0000-0000-0000-000000000001",
      "productoNombre": "Pechuga de Pollo",
      "fecha": "2024-01-02",
      "martes": 10,
      "miercoles": 8,
      "jueves": 12,
      "viernes": 10,
      "sabado": 6,
      "domingo": 4,
      "lunes": 5
    }
  ],
  "confirmadoPor": null,
  "fechaConfirmacion": null
}
```

### 8.2 Listar órdenes (paginado)

```
GET /orden_compra?page=0&size=9
```

**Response 200:**
```json
{
  "content": [
    {
      "id": "e1b20001-0000-0000-0000-000000000001",
      "cliente": "H.G.Z. No. 1",
      "contrato": "CONTRATO-IMSS-2024-001",
      "partida": "CARNES",
      "fechaInicioSemana": "2024-01-02",
      "detalles": [...],
      "confirmadoPor": "admin",
      "fechaConfirmacion": "2024-01-03T10:30:00",
      "tieneFactura": false,
      "estado": "PENDIENTE",
      "totalNotas": 7,
      "notasFirmadas": 5,
      "totalCancelaciones": 2,
      "cancelacionesValidadas": 1
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "size": 9,
  "number": 0
}
```

### 8.3 Listar por fecha

```
GET /orden_compra?fecha=2024-01-04&page=0&size=9
```

Filtra órdenes cuyo rango `(fechaInicioSemana, fechaFinSemana)` contiene la fecha dada.

### 8.4 Buscar por ID

```
GET /orden_compra/{id}
```

**Response 200:** DTO detallado de la orden.

### 8.5 Actualizar

```
PATCH /orden_compra/{id}
```

**Request:**
```json
{
  "partida": "CARNES",
  "detalles": [
    {
      "fecha": "2024-01-02",
      "producto": "a1b20001-0000-0000-0000-000000000001",
      "martes": 15,
      "miercoles": 10
    }
  ]
}
```

**Response 200:** DTO actualizado.

### 8.6 Eliminar (soft-delete)

```
DELETE /orden_compra/{id}
```

**Response 204:** No content.

### 8.7 Confirmar orden

```
POST /orden_compra/{id}/confirmar
```

**Response 200:**
```json
{
  "id": "e1b20001-0000-0000-0000-000000000001",
  "confirmadoPor": "admin",
  "fechaConfirmacion": "2024-01-03T10:30:00",
  ...
}
```

### 8.8 Listar notas de una orden

```
GET /orden_compra/{id}/notas
```

**Response 200:**
```json
[
  {
    "id": "f1b20001-0000-0000-0000-000000000001",
    "folio": 9001,
    "fecha": "2024-01-02T00:00:00",
    "cliente": "H.G.Z. No. 1",
    "partida": "CARNES",
    "dia": "martes",
    "firmada": false,
    "totalGeneral": 890.00,
    "detalles": [...]
  }
]
```

### 8.9 Generar todas las notas

```
POST /orden_compra/{id}/generar-notas
```

**Response 200:**
```json
[
  { "id": "...", "folio": 9001, "dia": "martes", ... },
  { "id": "...", "folio": 9002, "dia": "miercoles", ... },
  { "id": "...", "folio": 9003, "dia": "jueves", ... },
  { "id": "...", "folio": 9004, "dia": "viernes", ... },
  { "id": "...", "folio": 9005, "dia": "sabado", ... },
  { "id": "...", "folio": 9006, "dia": "domingo", ... },
  { "id": "...", "folio": 9007, "dia": "lunes", ... }
]
```

---

## 9. Notas de Venta — `/notaventas` [AUTH]

### 9.1 Registrar nota (manual)

```
POST /notaventas
```

**Request:**
```json
{
  "clienteId": "c1b20001-0000-0000-0000-000000000001",
  "partida": "CARNES",
  "detalles": [
    { "cantidad": 10, "productoId": "a1b20001-0000-0000-0000-000000000001" }
  ]
}
```

**Response 201:** DTO de la nota creada.

### 9.2 Generar nota desde orden

```
POST /notaventas/generar-desde-orden
```

**Request:**
```json
{
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "dia": "martes"
}
```

**Response 201:**
```json
{
  "id": "f1b20001-0000-0000-0000-000000000001",
  "folio": 9001,
  "fecha": "2024-01-02T00:00:00",
  "cliente": "H.G.Z. No. 1",
  "partida": "CARNES",
  "dia": "martes",
  "firmada": false,
  "detalle": null,
  "detalles": [
    { "cantidad": 10, "producto": "Pechuga de Pollo", "precio": 89.00, "subTotal": 890.00 }
  ],
  "totalGeneral": 890.00
}
```

### 9.3 Listar notas (paginado)

```
GET /notaventas?page=0&size=9&sort=fecha,desc
```

**Response 200:** Page de notas con folio, cliente, fecha, total, estado de firma.

### 9.4 Buscar por ID

```
GET /notaventas/{id}
```

**Response 200:** DTO detallado de la nota.

### 9.5 Actualizar

```
PATCH /notaventas/{id}
```

**Request:**
```json
{
  "partida": "CARNES",
  "detalles": [
    { "cantidad": 12, "producto": "Pechuga de Pollo" }
  ]
}
```

**Response 200:** DTO actualizado.

### 9.6 Eliminar (soft-delete)

```
DELETE /notaventas/{id}
```

**Response 204:** No content. Retorna 409 si la OC ya está facturada.

### 9.7 Firmar nota

```
POST /notaventas/{id}/firmar
```

**Response 200:** DTO con `firmada: true`.

### 9.8 Actualizar detalle (incidencias)

```
PATCH /notaventas/{id}/detalle
```

**Request:**
```json
{
  "detalle": "Llegó tarde, faltó pechuga de pollo"
}
```

**Response 200:** DTO con `detalle` actualizado.

---

## 10. Cancelaciones — `/cancelaciones` [AUTH]

### 10.1 Crear cancelación

```
POST /cancelaciones
```

**Request:**
```json
{
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "dia": "martes",
  "detalles": [
    { "productoId": "a1b20001-0000-0000-0000-000000000001", "cantidadCancelada": 3 }
  ]
}
```

**Response 201:**
```json
{
  "id": "g1b20001-0000-0000-0000-000000000001",
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "dia": "martes",
  "fechaCreacion": "2024-01-04T14:30:00",
  "creadoPor": "admin",
  "validadoPor": null,
  "fechaValidacion": null,
  "detalles": [
    { "id": "...", "producto": "a1b20001-...", "productoNombre": "Pechuga de Pollo", "cantidadCancelada": 3 }
  ]
}
```

### 10.2 Listar por orden

```
GET /cancelaciones/orden/{ordenCompraId}
```

**Response 200:** Lista de cancelaciones.

### 10.3 Validar cancelación

```
POST /cancelaciones/{id}/validar
```

**Response 200:**
```json
{
  "id": "g1b20001-0000-0000-0000-000000000001",
  "validadoPor": "admin",
  "fechaValidacion": "2024-01-04T15:00:00",
  ...
}
```

### 10.4 Eliminar (soft-delete)

```
DELETE /cancelaciones/{id}
```

**Response 204:** No content. Retorna 409 si ya está validada.

### 10.5 Reconstruir notas

```
POST /cancelaciones/reconstruir/{ordenCompraId}
```

Reconstruye todas las notas de una orden aplicando todas las cancelaciones validadas. Útil para recuperación de datos.

**Response 200:** Lista de `DatosDetalleNota` con cantidades recalculadas.

---

## 11. Extras — `/extras` [AUTH]

### 11.1 Crear extra

```
POST /extras
```

**Request:**
```json
{
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "dia": "martes",
  "detalles": [
    { "productoId": "a1b20001-0000-0000-0000-000000000002", "cantidad": 5 }
  ]
}
```

**Response 201:**
```json
{
  "id": "h1b20001-0000-0000-0000-000000000001",
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "dia": "martes",
  "fecha": "2024-01-02",
  "folio": 1,
  "firmada": false,
  "fechaCreacion": "2024-01-04T16:00:00",
  "creadoPor": "admin",
  "detalles": [
    { "id": "...", "producto": "a1b20001-...", "productoNombre": "Milanesa de Pollo", "cantidad": 5 }
  ]
}
```

### 11.2 Listar por orden

```
GET /extras/orden/{ordenCompraId}
```

**Response 200:** Lista de extras.

### 11.3 Firmar extra

```
POST /extras/{id}/firmar
```

**Response 200:** DTO con `firmada: true`.

### 11.4 Eliminar (soft-delete)

```
DELETE /extras/{id}
```

**Response 204:** No content. Retorna 409 si ya está firmado.

---

## 12. Facturas — `/facturas` [AUTH]

### 12.1 Generar factura principal

```
POST /facturas
```

**Request:**
```json
{
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001"
}
```

**Response 201:**
```json
{
  "id": "i1b20001-0000-0000-0000-000000000001",
  "folio": 1,
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001",
  "cliente": "H.G.Z. No. 1",
  "contrato": "CONTRATO-IMSS-2024-001",
  "partida": "CARNES",
  "fechaCreacion": "2024-01-05T12:00:00",
  "totalGeneral": 2670.00,
  "esExtras": false,
  "detalles": [
    {
      "id": "...",
      "productoNombre": "Pechuga de Pollo",
      "cantidadTotal": 30,
      "precioVenta": 89.00,
      "subtotal": 2670.00
    }
  ]
}
```

### 12.2 Listar facturas

```
GET /facturas
```

**Response 200:** Lista de todas las facturas activas ordenadas por fecha descendente.

### 12.3 Obtener por ID

```
GET /facturas/{id}
```

**Response 200:** DTO de la factura.

### 12.4 Obtener por OC (factura principal)

```
GET /facturas/por-orden/{ordenCompraId}
```

**Response 200:** Factura principal de la OC (esExtras=false) o 404.

### 12.5 Generar factura de extras

```
POST /facturas/extras
```

**Request:**
```json
{
  "ordenCompraId": "e1b20001-0000-0000-0000-000000000001"
}
```

**Response 201:** Factura con `esExtras: true`.

### 12.6 Obtener factura de extras por OC

```
GET /facturas/extras/por-orden/{ordenCompraId}
```

**Response 200:** Factura de extras de la OC o 404.

---

## 13. Reportes — `/reportes` [AUTH]

### 13.1 Reporte de Producción Carnes

```
GET /reportes/produccion-carne?semana=2024-01-02
```

**Response 200:**
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
            {
              "productoNombre": "Pechuga de Pollo",
              "cantidad": 20,
              "unidadMedida": "KILO"
            }
          ],
          "totalDia": 20
        }
      ]
    }
  ]
}
```

---

## 14. Usuarios (Admin) — `/usuarios` [ADMIN]

### 14.1 Listar usuarios

```
GET /usuarios
```

**Response 200:**
```json
[
  {
    "id": "j1b20001-0000-0000-0000-000000000001",
    "username": "admin",
    "role": "ADMIN",
    "activo": true,
    "nombreCompleto": "Administrador del Sistema",
    "correo": "admin@laveronica.com",
    "numero": "555-0100",
    "cargo": "Gerente"
  }
]
```

### 14.2 Buscar usuario por ID

```
GET /usuarios/{id}
```

**Response 200:**
```json
{
  "id": "j1b20001-0000-0000-0000-000000000001",
  "username": "admin",
  "role": "ADMIN",
  "activo": true,
  "nombreCompleto": "Administrador del Sistema",
  "correo": "admin@laveronica.com",
  "numero": "555-0100",
  "cargo": "Gerente",
  "permisos": [
    { "modulo": "PRODUCTOS", "accion": "CREAR" },
    { "modulo": "PRODUCTOS", "accion": "LEER" },
    { "modulo": "PRODUCTOS", "accion": "ACTUALIZAR" },
    { "modulo": "PRODUCTOS", "accion": "ELIMINAR" }
  ]
}
```

### 14.3 Crear usuario

```
POST /usuarios
```

**Request:**
```json
{
  "username": "operador1",
  "password": "pass123",
  "role": "USER",
  "nombreCompleto": "Operador Uno",
  "correo": "operador1@laveronica.com",
  "numero": "555-0200",
  "cargo": "Operador"
}
```

**Response 201:** `DatosRespuestaAuth` con token.

### 14.4 Asignar permisos

```
PUT /usuarios/{id}/permisos
```

**Request:**
```json
{
  "usuarioId": "j1b20001-0000-0000-0000-000000000001",
  "permisos": [
    { "modulo": "PRODUCTOS", "accion": "LEER" },
    { "modulo": "CLIENTES", "accion": "LEER" }
  ]
}
```

**Response 204:** No content.

### 14.5 Toggle activo/inactivo

```
PATCH /usuarios/{id}/toggle
```

**Response 204:** No content. Cambia `activo` de `true` a `false` o viceversa.

### 14.6 Actualizar usuario

```
PATCH /usuarios/{id}
```

**Request:**
```json
{
  "nombreCompleto": "Admin Actualizado",
  "correo": "admin.nuevo@laveronica.com",
  "password": "nuevaPassword123",
  "role": "ADMIN"
}
```

**Response 200:** DTO actualizado.

---

## 15. Super Admin — `/admin/super` [ADMIN]

### 15.1 Eliminar nota por ID

```
DELETE /admin/super/nota/{id}
```

**Response 200:**
```json
{
  "mensaje": "Nota eliminada correctamente"
}
```

### 15.2 Eliminar nota por folio

```
DELETE /admin/super/nota/folio/{folio}
```

**Response 200:**
```json
{
  "mensaje": "Nota eliminada correctamente"
}
```

### 15.3 Eliminar orden por ID

```
DELETE /admin/super/orden/{id}
```

**Response 200:**
```json
{
  "mensaje": "Orden eliminada correctamente"
}
```

### 15.4 Eliminar cancelación por ID

```
DELETE /admin/super/cancelacion/{id}
```

**Response 200:**
```json
{
  "mensaje": "Cancelación eliminada correctamente"
}
```

---

## Apéndice: Formato de errores

```json
// 400 - Bad Request (validación @Valid)
{
  "codigo": 400,
  "mensaje": "Error de validación",
  "errores": [
    "El nombre no puede estar vacío",
    "El RFC debe tener formato válido"
  ]
}

// 404 - Not Found
{
  "codigo": 404,
  "mensaje": "Recurso no encontrado: Producto con ID a1b20001-... no existe"
}

// 409 - Conflict
{
  "codigo": 409,
  "mensaje": "Ya existe una orden activa para el cliente H.G.Z. No. 1 en la semana 2024-01-02"
}

// 500 - Internal Server Error
{
  "codigo": 500,
  "mensaje": "Error interno del servidor"
}
```

## Apéndice: Estados de OrdenCompra

Los estados se calculan en el backend y se devuelven en el campo `estado` del DTO `DatosListarOrdenCompra`:

| Estado | Significado |
|--------|-------------|
| `PENDIENTE` | Orden creada pero no confirmada |
| `FIRMAS_PENDIENTES` | Confirmada pero con notas sin firmar |
| `CANCELACIONES_PENDIENTES` | Confirmada con cancelaciones sin validar |
| `PREFACTURA` | Todo listo para facturar |
| `LISTO` | Ya tiene factura generada (bloqueado) |
