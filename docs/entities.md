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
| notaVentas | List\<NotaVenta\> | — | mappedBy=cliente |
| contratos | List\<Contrato\> | — | mappedBy=cliente |

## Categoria

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| nombre | String | `nombre` | @NotNull |
| partida | Partida (enum) | `partida` | STRING, not null |
| activo | Boolean | `activo` | Default true |
| productos | List\<Producto\> | — | mappedBy=categoria |

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
| notas | List\<NotaVenta\> | — | mappedBy=contrato |

## OrdenCompra

| Campo | Tipo | Columna | Restricciones |
|-------|------|---------|---------------|
| id | String (UUID) | `id` | PK, auto-generado |
| cliente | Cliente | `cliente_id` | FK → clientes, not null |
| contrato | Contrato | `contrato_id` | FK → contratos, not null |
| partida | Partida (enum) | `partida` | STRING, not null |
| fechaInicioSemana | LocalDate | `fecha_inicio_semana` | Not null |
| fechaFinSemana | LocalDate | `fecha_fin_semana` | Not null |
| detalles | List\<OrdenCompraDetalle\> | — | Cascade ALL, orphanRemoval |
| activo | Boolean | `activo` | — |

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
