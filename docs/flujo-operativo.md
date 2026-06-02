# Análisis del Flujo Operativo - Carnicería La Verónica

## Estructura del Excel (38 hojas)

### Hojas Principales

| Hoja | Propósito | Datos |
|------|-----------|-------|
| **ORDENES** | Captura de órdenes semanales por clínica | 4 bloques (IMSS 1, 2, 3, GUARDERÍA) × 25 productos × 7 días |
| **PRECIOS** | Tabla de referencia de precios | 25 productos con precio costo y precio venta |
| **Totales** | Resumen financiero por clínica | Notas diarias + Total Compra + Total Venta + Utilidad |
| **IMSS 1/2/3** | Consolidación semanal por clínica | Cantidades + Importes + Folios de nota por día |
| **GUARDERÍA** | Consolidación semanal Guardería | Mismo formato que IMSS |
| **PEDIDOS 2000** | Pedido diario a carniceros | Productos con cantidad > 0, organizados por día y clínica |
| **Ped Guarde** | Pedido Guardería | Similar a PEDIDOS 2000 |
| **Kilos** | Resumen kilos por producto por clínica | Kilos totales semanales |
| **01#/02#/03# + día** | Notas de venta diarias | Folio, fecha, cliente, detalle productos, total |

### Hojas Diarias (Notas de Venta)

```
01# = IMSS 1 (H.G.Z. No. 1)
02# = IMSS 2 (Clínica IMSS 2)  
03# = IMSS 3 (Clínica IMSS 3)
Gua# = GUARDERÍA

Cada una tiene 7 hojas (Lun, Mar, Mie, Jue, Vie, Sab, Dom)
```

## Flujo Operativo

```
┌─────────────────────────────────────────────────────────┐
│  PASO 1: CAPTURA DE ÓRDENES SEMANALES                  │
│  Hoja: ORDENES                                          │
│                                                         │
│  Seleccionar: Clínica → Capturar cantidades por         │
│  producto por día de la semana                          │
│                                                         │
│  Resultado: Tabla con cantidades por día por producto   │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  PASO 2: GENERACIÓN DE NOTAS DE VENTA DIARIAS          │
│  Hojas: 01# Mar, 01# Mie, 02# Mar, etc.               │
│                                                         │
│  Por cada día por clínica:                              │
│  - Folio único (9000, 9001, 9002...)                   │
│  - Fecha del día                                        │
│  - Cliente (IMSS 1, IMSS 2, IMSS 3, GUARDERÍA)        │
│  - Productos con: cantidad × precio_unitario = total    │
│  - Total general de la nota                             │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  PASO 3: CONSOLIDACIÓN SEMANAL POR CLÍNICA             │
│  Hojas: IMSS 1, IMSS 2, IMSS 3, GUARDERÍA             │
│                                                         │
│  Por cada producto:                                     │
│  - Cantidades por día (Lun-Dom)                         │
│  - Total semana (suma de días)                          │
│  - Importe compra = cantidad × precio_costo             │
│  - Importe venta = cantidad × precio_venta              │
│  - Folio de nota de venta por día                       │
│                                                         │
│  Totales: Importe compra semanal, Importe venta semanal │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  PASO 4: GENERACIÓN DE PEDIDOS A PROVEEDOR              │
│  Hojas: PEDIDOS 2000, Ped Guarde                        │
│                                                         │
│  Por cada día:                                          │
│  - Consolida cantidades de todas las clínica            │
│  - Solo productos con cantidad > 0                      │
│  - Se envía 1 DÍA ANTES (hoy piden para mañana)        │
│  - Entrega: 7:00 AM (multa si no llega)                │
│  - Carne de res: se compra por MEDIAS RESES            │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│  PASO 5: SEGUIMIENTO FINANCIERO                        │
│  Hoja: TOTALES                                          │
│                                                         │
│  Por clínica:                                           │
│  - Notas diarias (ingresos por día)                     │
│  - Total Compra (costo total semana)                    │
│  - Total Venta (ingreso total semana)                   │
│  - Utilidad = Total Venta - Total Compra                │
│                                                         │
│  Total General:                                         │
│  - Compra: $100,615                                     │
│  - Venta: $167,449                                      │
│  - Utilidad: $53,885                                    │
│  - Margen: 32.2%                                        │
└─────────────────────────────────────────────────────────┘
```

## Mapeo a Entidades del Sistema

| Excel | Entidad Sistema | Relación |
|-------|----------------|----------|
| ORDENES (por clínica) | `OrdenCompra` + `OrdenCompraDetalle` | 1 orden por semana por clínica |
| PRECIOS | `Producto.precioCompra` + `precioVenta` | Referencia por producto |
| IMSS 1/2/3, GUARDERÍA | `NotaVenta` + `NotaVentaDetalle` | 1 nota por día por clínica |
| PEDIDOS 2000 | Reporte derivado | Consolidación de cantidades por día |
| Kilos | Reporte derivado | Suma de kilos por producto por clínica |
| Totales | Reporte derivado | Consolidación financiera por contrato |
| 01# Mar, etc. | `NotaVenta` (detalle) | Nota impresa con folio |

## Fórmulas Clave del Excel

```
// Por cada producto por día:
Importe Compra = Cantidad × Precio Costo
Importe Venta  = Cantidad × Precio Venta

// Por clínica por semana:
Total Compra Clínica = Σ(Importe Compra diario)
Total Venta Clínica  = Σ(Importe Venta diario)
Utilidad Clínica     = Total Venta - Total Compra

// Total general:
Total Compra = Σ(Total Compra por clínica)
Total Venta  = Σ(Total Venta por clínica)
Utilidad     = Total Venta - Total Compra
Margen %     = (Utilidad / Total Venta) × 100

// Kilos por producto:
Kilos Semana = Σ(Kilos por clínica)
Kilos Clínica = Σ(Cantidades por día)
```

## Reglas de Negocio

1. **Folio secuencial**: Cada nota de venta tiene un folio único e incremental
2. **Pedido 1 día antes**: Los pedidos a proveedor se hacen el día anterior
3. **Entrega 7 AM**: Si no llega a las 7, hay multa
4. **Res por media res**: La carne de res se compra por media res, no por kilo
5. **Precios fijos semana**: Los precios se establecen por semana (no cambian diario)
6. **Contrato anual**: Los clientes tienen contrato con presupuesto anual
7. **Control por partida**: Los productos se agrupan por partida (CARNES, LACTEOS, etc.)

## Prioridades de Implementación

### Fase 1: Captura de Órdenes (COMPLETADO)
- [x] Formulario tipo Excel: Cliente → Partida → Productos × Días
- [x] Guardado de OrdenCompra con detalles por día
- [x] Vista de confirmación tipo tabla

### Fase 2: Notas de Venta
- [ ] Crear nota desde orden existente (pre-cargar cantidades del día)
- [ ] Generación automática de folio secuencial
- [ ] Impresión de nota con formato (encabezado, detalle, total)
- [ ] Vincular nota a orden de compra

### Fase 3: Consolidación y Reportes
- [ ] Reporte consolidado por clínica (como hoja IMSS 1)
- [ ] Reporte Kilos (como hoja Kilos)
- [ ] Reporte PEDIDOS 2000 (pedido diario a proveedor)
- [ ] Reporte Totales (resumen financiero)

### Fase 4: Control Presupuestal
- [ ] Seguimiento de consumo vs presupuesto de contrato
- [ ] Alertas cuando se acerca al límite
- [ ] Dashboard con métricas en tiempo real
