# Ajustes Arquitectónicos — SOLID & ACID

## Resumen

Se auditaron 30+ archivos del backend identificando violaciones a los principios SOLID y ACID. Se corrigieron 8
problemas críticos/mayores. Todos los cambios están respaldados por **153 tests pasando** y build de frontend limpio.

---

## 1. `NotaVentaService.eliminarNota()` — soft-delete no persistido

| Aspecto | Detalle |
|---------|---------|
| **Código** | `services/NotaVentaService.java:191-196` |
| **Principio** | ACID-Durability (Atomicidad) |
| **Severidad** | 🔴 CRÍTICO — pérdida de datos |
| **Problema** | `nota.setActivo(false)` sin `notaVentaRepository.save(nota)`. El soft-delete nunca se escribía en BD. |
| **Solución** | Agregar `notaVentaRepository.save(nota)` después de `setActivo(false)`. |
| **Cambio** | 1 línea añadida en `NotaVentaService.java:197` |

---

## 2. `NotaCancelacionService.validarCancelacion()` — orden de operaciones incorrecto

| Aspecto | Detalle |
|---------|---------|
| **Código** | `services/NotaCancelacionService.java:84-97` |
| **Principio** | ACID-Atomicity |
| **Severidad** | 🔴 CRÍTICO — inconsistencia de datos |
| **Problema** | La cancelación se persistía (`cancelacionRepository.save(nc)`) **antes** de ajustar la nota. Si `aplicarCancelacionANota()` fallaba, la cancelación quedaba guardada sin aplicar. |
| **Solución** | Mover `aplicarCancelacionANota(nc)` **antes** del `save()`. Si el ajuste falla, la transacción completa hace rollback. |
| **Cambio** | Reordenamiento en `NotaCancelacionService.java` |

---

## 3. Cancelación sin validar que no exceda cantidad disponible

| Aspecto | Detalle |
|---------|---------|
| **Código** | `services/NotaCancelacionService.java:99-125` |
| **Principio** | ACID-Consistency |
| **Severidad** | 🔴 CRÍTICO — datos negativos |
| **Problema** | `aplicarCancelacionANota()` usaba `removeIf` y `Math.round(cancelQty)` — si cancelQty > cantidad, la nota quedaba con cantidades negativas. |
| **Solución** | Reemplazar `removeIf` con iteración explícita + validación: si `nuevaCant < 0` → lanza `RuntimeException`. También valida que el producto exista en la nota. |
| **Cambio** | Reescritura completa de `aplicarCancelacionANota()` |

---

## 4. Generación de folios sin protección contra condiciones de carrera

| Aspecto | Detalle |
|---------|---------|
| **Código** | `repositories/ExtraRepository.java:15`, `NotaVentaRepository.java:19`, `FacturaRepository.java:15` |
| **Principio** | ACID-Isolation / Consistency |
| **Severidad** | 🔴 CRÍTICO — duplicación de folios |
| **Problema** | `SELECT COALESCE(MAX(folio), 0) + 1` no es atómico. Dos requests concurrentes pueden leer el mismo MAX y generar folios duplicados. Además, `extras.folio` y `facturas.folio` **no tenían constraint UNIQUE**, permitiendo duplicados silenciosos. |
| **Solución** | 1) Agregar `@Lock(LockModeType.PESSIMISTIC_WRITE)` a las queries de `findMaxFolio()`/`obtenerMaxFolio()`. 2) Migración V19 con `ALTER TABLE ... ADD CONSTRAINT uq_... UNIQUE (folio)` para extras y facturas. |
| **Cambio** | 3 repositorios + 1 migración nueva |

---

## 5. TOCTOU Race Condition en generación de notas desde OC

| Aspecto | Detalle |
|---------|---------|
| **Código** | `services/NotaVentaService.java:72-126` |
| **Principio** | ACID-Isolation |
| **Severidad** | 🔴 CRÍTICO — duplicación de datos |
| **Problema** | `generarNotaDesdeOrden()` verifica `existsByOrdenCompraIdAndActivoTrue` y luego crea la nota. Entre la verificación y la inserción, otro request puede crear otra nota para la misma OC+día. |
| **Solución** | 1) Nuevo método `findByIdAndActivoTrueWithLock()` en `OrdenCompraRespository` con `@Lock(PESSIMISTIC_WRITE)`. 2) `generarNotaDesdeOrden()` usa este método para serializar el acceso a la OC. |
| **Cambio** | `OrdenCompraRespository.java` + `NotaVentaService.java` + `NotaVentaServiceTest.java` |

---

## 6. Lógica de día-de-semana duplicada (DRY)

| Aspecto | Detalle |
|---------|---------|
| **Código** | `ExtraService.java:102-113`, `NotaVentaService.java:128-149`, `NotaCancelacionService.java:195-206` |
| **Principio** | SOLID-OCP |
| **Severidad** | 🔴 CRÍTICO — mantenibilidad |
| **Problema** | La misma lógica de `switch` sobre nombres de día en español estaba duplicada en 3 servicios. Cualquier cambio requería modificar 3 archivos. |
| **Solución** | El `DiaSemana` enum existente se expandió con métodos: |
| | • `calcularFecha(LocalDate inicioSemana)` — reemplaza `calcularFecha()` en ExtraService y NotaVentaService |
| | • `getCantidad(OrdenCompraDetalle)` — reemplaza `getCantidadPorDia()` en NotaVentaService y NotaCancelacionService |
| | • `fromString(String)` — parseo centralizado con validación |
| **Cambio** | `enums/DiaSemana.java` + 3 servicios |

---

## 7. Duplicación 70% entre `generarFactura()` y `generarFacturaExtras()`

| Aspecto | Detalle |
|---------|---------|
| **Código** | `services/FacturaService.java:36-199` |
| **Principio** | SOLID-OCP |
| **Severidad** | 🔴 CRÍTICO — mantenibilidad |
| **Problema** | Dos métodos que construyen facturas compartían ~70% del código (creación de entidad, cálculo de totales, persistencia). |
| **Solución** | Extraer método privado `crearFactura(OrdenCompra, Map<String, Double>, Map<String, BigDecimal>, boolean esExtras)` que encapsula la creación de la factura. Los métodos públicos solo agregan la lógica específica de agregación. |
| **Cambio** | `FacturaService.java` — reducción de 199→160 líneas (~20%) |

---

## 8. Controllers con acoplamiento directo a `SecurityContextHolder`

| Aspecto | Detalle |
|---------|---------|
| **Código** | `controller/ExtraController.java:29,42`, `NotaCancelacionController.java:30,45` |
| **Principio** | SOLID-SRP |
| **Severidad** | 🔴 CRÍTICO — testabilidad |
| **Problema** | Los controllers obtenían el username mediante `SecurityContextHolder.getContext().getAuthentication().getName()` inline. Esto acopla el controller a Spring Security y dificulta los tests. |
| **Solución** | Usar `@AuthenticationPrincipal UserDetails userDetails` como parámetro del método del controller. Spring inyecta automáticamente el usuario autenticado. |
| **Cambio** | `ExtraController.java` + `NotaCancelacionController.java` |

---

## Archivos modificados

```
Backend:
  src/main/java/.../enums/DiaSemana.java          (expandido)
  src/main/java/.../services/ExtraService.java      (DiaSemana, imports)
  src/main/java/.../services/NotaVentaService.java  (save, DiaSemana, WithLock)
  src/main/java/.../services/NotaCancelacionService.java (orden, DI, DiaSemana)
  src/main/java/.../services/FacturaService.java    (extraer crearFactura)
  src/main/java/.../repositories/ExtraRepository.java    (@Lock)
  src/main/java/.../repositories/NotaVentaRepository.java (@Lock)
  src/main/java/.../repositories/FacturaRepository.java  (@Lock)
  src/main/java/.../repositories/OrdenCompraRespository.java (WithLock)
  src/main/java/.../controller/ExtraController.java       (@AuthenticationPrincipal)
  src/main/java/.../controller/NotaCancelacionController.java (@Auth + constructor DI)
  src/main/resources/db/migration/V19__add_folio_unique_constraints.sql (nuevo)

Backend (tests):
  src/test/java/.../service/NotaVentaServiceTest.java   (WithLock)

Frontend:
  (sin cambios — solo backend)

Docs:
  docs/04-ajustes-arquitectonicos.md (este archivo)
```

---

## Pendientes (no críticos, para siguiente iteración)

| # | Ítem | Prioridad |
|---|------|-----------|
| 1 | Migrar `NotaVentaDetalleService` de field injection a constructor injection | Baja |
| 2 | Agregar `@Version` para optimistic locking en entidades raíz | Media |
| 3 | Introducir `@MappedSuperclass BaseEntity` con `id`/`activo` | Baja |
| 4 | Segregar interfaces de repositorio (ISP) | Baja |
| 5 | Reemplazar `Double` por `BigDecimal` en campos de cantidad | Media |
| 6 | Agregar FK `producto_id` a `FacturaDetalle` | Media |
| 7 | Refactor mayor: unificar NotaVenta/Extra/Cancelacion en una sola entidad `Nota` | Alta |
