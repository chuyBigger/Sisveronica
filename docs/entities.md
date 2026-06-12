# Entidades JPA

## Cliente

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| nombre | String | `nombre` | — |
| rfc | String | `rfc` | Unique |
| calle | String | `calle` | — |
| numero | Integer | `numero` | — |
| fraccionamiento | String | `fraccionamiento` | — |
| cp | String | `c_p` | — |
| municipio | String | `municipio` | — |
| estado | String | `estado` | — |
| activo | boolean | `activo` | Default true |

## Categoria

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| nombre | String | `nombre` | @NotNull |
| partida | Partida (enum) | `partida` | STRING, not null |
| activo | Boolean | `activo` | Default true |

## Producto

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| codigo | String | `codigo` | Unique |
| nombre | String | `nombre` | Not null, unique |
| partida | Partida (enum) | `partida` | STRING, not null |
| categoria | Categoria | `categoria_id` | FK → categorias |
| unidadMedida | UnidadMedida (enum) | `unidad_medida` | STRING, not null |
| precioCompra | BigDecimal | `precio_compra` | — |
| precioVenta | BigDecimal | `precio_venta` | — |
| activo | Boolean | `activo` | Default true |

## Contrato

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| contrato | String | `contrato` | Unique, not null, length=100 |
| cliente | Cliente | `cliente_id` | FK → clientes, not null |
| fechaInicio | LocalDate | `fecha_inicio` | Not null |
| fechaTermino | LocalDate | `fecha_termino` | Not null |
| presupuesto | BigDecimal | `presupuesto` | precision=12, scale=2 |
| activo | Boolean | `activo` | — |

## OrdenCompra

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| cliente | Cliente | `cliente_id` | FK → clientes, not null |
| contrato | Contrato | `contrato_id` | FK → contratos |
| partida | Partida (enum) | `partida` | STRING, not null |
| fechaInicioSemana | LocalDate | `fecha_inicio_semana` | Not null |
| fechaFinSemana | LocalDate | `fecha_fin_semana` | Not null |
| detalles | List\<OrdenCompraDetalle\> | — | Cascade ALL, orphanRemoval |
| activo | Boolean | `activo` | — |
| confirmadoPor | String | `confirmado_por` | Usuario que confirmó |
| fechaConfirmacion | LocalDateTime | `fecha_confirmacion` | — |

## OrdenCompraDetalle

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| ordenCompra | OrdenCompra | `orden_compra_id` | FK → orden_compras |
| fecha | LocalDate | `fecha` | Not null |
| producto | Producto | `producto_id` | FK → productos, not null |
| lunes..domingo | Double | `lunes`..`domingo` | Cantidades por día |
| activo | boolean | `activo` | Default true |

## NotaVenta

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| folio | Integer | `folio` | Unique, not null |
| cliente | Cliente | `cliente_id` | FK → clientes |
| contrato | Contrato | `contrato_id` | FK → contratos (opcional) |
| ordenCompra | OrdenCompra | `orden_compra_id` | FK → orden_compras (opcional) |
| fecha | LocalDateTime | `fecha` | Not null |
| partida | Partida (enum) | `partida` | STRING, not null |
| dia | String | `dia` | Día de la semana (lunes–domingo) |
| firmada | Boolean | `firmada` | Default false |
| detalle | String | `detalle` | Texto, nullable |
| detalles | List\<NotaVentaDetalle\> | — | Cascade ALL, orphanRemoval |
| totalGeneral | BigDecimal | `total_general` | Not null |
| activo | Boolean | `activo` | — |

## NotaVentaDetalle

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| cantidad | Integer | `cantidad` | Not null |
| producto | Producto | `producto_id` | FK → productos |
| precioVenta | BigDecimal | `precio_venta` | — |
| subTotal | BigDecimal | `sub_total` | — |
| activo | Boolean | `activo` | — |
| notaVenta | NotaVenta | `notaventa_id` | FK → nota_ventas |

## NotaCancelacion

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| ordenCompra | OrdenCompra | `orden_compra_id` | FK → orden_compras, not null |
| dia | String | `dia` | Día de la semana |
| creadoPor | String | `creado_por` | Usuario que creó |
| fechaCreacion | LocalDateTime | `fecha_creacion` | — |
| validadoPor | String | `validado_por` | Usuario que validó (null = pendiente) |
| fechaValidacion | LocalDateTime | `fecha_validacion` | — |
| activo | Boolean | `activo` | — |

## NotaCancelacionDetalle

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| notaCancelacion | NotaCancelacion | `nota_cancelacion_id` | FK → nota_cancelaciones |
| producto | Producto | `producto_id` | FK → productos |
| cantidadCancelada | Double | `cantidad_cancelada` | — |
| activo | Boolean | `activo` | — |

## Factura

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| folio | Integer | `folio` | Secuencial auto |
| ordenCompra | OrdenCompra | `orden_compra_id` | FK → orden_compras, not null |
| cliente | String | `cliente` | Denormalizado |
| contrato | String | `contrato` | Denormalizado |
| partida | String | `partida` | Denormalizado |
| fechaCreacion | LocalDateTime | `fecha_creacion` | — |
| totalGeneral | BigDecimal | `total_general` | — |
| activo | Boolean | `activo` | Default true |

## FacturaDetalle

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| factura | Factura | `factura_id` | FK → facturas |
| productoNombre | String | `producto_nombre` | Denormalizado |
| cantidadTotal | Double | `cantidad_total` | Suma de todas las notas |
| precioVenta | BigDecimal | `precio_venta` | — |
| subtotal | BigDecimal | `subtotal` | cantidadTotal × precioVenta |

## Usuario

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| username | String | `username` | Unique, not null |
| password | String | `password` | BCrypt hash |
| role | Role (enum) | `role` | STRING (ADMIN/USER/VIEWER) |
| activo | Boolean | `activo` | Default true |
