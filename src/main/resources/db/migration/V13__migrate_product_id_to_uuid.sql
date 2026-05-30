-- V13: Migrar producto id de BIGINT a UUID (VARCHAR(36))
-- Se eliminan datos semilla de V12 que usaban BIGINT y se reinsertan con UUIDs

-- 1. Eliminar datos que referencian productos (orden FK inverso)
DELETE FROM nota_venta_detalles;
DELETE FROM orden_compra_detalles;

-- 2. Eliminar constraints FK de hibernate si existen
ALTER TABLE nota_venta_detalles DROP FOREIGN KEY IF EXISTS FKfh3gaxb9g4thwntoix23n9v9v;

-- 3. Eliminar productos (se reinsertan abajo con UUID)
DELETE FROM productos;

-- 4. Cambiar tipo de columna id a VARCHAR(36) para UUID
ALTER TABLE productos DROP PRIMARY KEY;
ALTER TABLE productos MODIFY id VARCHAR(36) NOT NULL;
ALTER TABLE productos ADD PRIMARY KEY (id);

-- 5. Cambiar tipo de columna producto_id en tablas dependientes
ALTER TABLE nota_venta_detalles MODIFY producto_id VARCHAR(36) NOT NULL;
-- orden_compra_detalles.producto_id ya es VARCHAR(100) desde V10

-- 6. Insertar productos con UUIDs fijos legibles
INSERT INTO productos (id, nombre, partida, categoria_id, unidad_medida, precio_compra, precio_venta, activo) VALUES
('uuid-prod-001', 'Leche Entera 1L', 'LACTEOS', 1, 'LITRO', 16.50, 22.00, TRUE),
('uuid-prod-002', 'Queso Panela 500g', 'LACTEOS', 2, 'KILO', 45.00, 68.00, TRUE),
('uuid-prod-003', 'Carne de Res Molida 1kg', 'CARNES', 3, 'KILO', 95.00, 135.00, TRUE),
('uuid-prod-004', 'Pechuga de Pollo 1kg', 'CARNES', 4, 'KILO', 78.00, 110.00, TRUE),
('uuid-prod-005', 'Manzana Roja 1kg', 'FRUTASYVERDURAS', 5, 'KILO', 25.00, 38.00, TRUE),
('uuid-prod-006', 'Plátano Tabasco 1kg', 'FRUTASYVERDURAS', 5, 'KILO', 12.00, 18.00, TRUE),
('uuid-prod-007', 'Jitomate Saladet 1kg', 'FRUTASYVERDURAS', 6, 'KILO', 18.00, 28.00, TRUE),
('uuid-prod-008', 'Cebolla Blanca 1kg', 'FRUTASYVERDURAS', 6, 'KILO', 14.00, 22.00, TRUE),
('uuid-prod-009', 'Frijol Refrito Lata 500g', 'ABARROTES', 7, 'PIEZA', 18.00, 26.00, TRUE),
('uuid-prod-010', 'Atún en Agua Lata 150g', 'ABARROTES', 7, 'PIEZA', 15.00, 22.00, TRUE),
('uuid-prod-011', 'Arroz Blanco 1kg', 'ABARROTES', 8, 'KILO', 20.00, 30.00, TRUE),
('uuid-prod-012', 'Salchicha de Pollo 500g', 'CARNES', 9, 'KILO', 42.00, 62.00, TRUE),
('uuid-prod-013', 'Chorizo de Res 500g', 'CARNES', 9, 'KILO', 55.00, 80.00, TRUE),
('uuid-prod-014', 'Jabón Líquido Trastes 1L', 'VARIOS', 10, 'LITRO', 28.00, 42.00, TRUE),
('uuid-prod-015', 'Cloro 1L', 'VARIOS', 10, 'LITRO', 12.00, 18.00, TRUE);

-- 7. Reinsertar orden_compra_detalles con UUIDs
INSERT INTO orden_compra_detalles (orden_compra_id, producto_id, fecha, lunes, martes, miercoles, jueves, viernes, sabado, domingo, activo) VALUES
(1, 'uuid-prod-009', '2026-05-25', 2, 3, 2, 3, 4, 5, 2, TRUE),
(1, 'uuid-prod-010', '2026-05-25', 3, 2, 4, 2, 3, 4, 1, TRUE),
(1, 'uuid-prod-011', '2026-05-25', 5, 4, 5, 4, 6, 7, 3, TRUE),
(2, 'uuid-prod-003', '2026-05-25', 4, 3, 5, 4, 6, 8, 4, TRUE),
(2, 'uuid-prod-004', '2026-05-25', 6, 5, 7, 5, 8, 10, 5, TRUE),
(2, 'uuid-prod-012', '2026-05-25', 3, 2, 3, 2, 4, 5, 2, TRUE),
(3, 'uuid-prod-005', '2026-05-25', 8, 6, 7, 8, 10, 12, 6, TRUE),
(3, 'uuid-prod-006', '2026-05-25', 10, 8, 9, 10, 12, 15, 8, TRUE),
(3, 'uuid-prod-007', '2026-05-25', 5, 4, 6, 5, 7, 8, 4, TRUE),
(4, 'uuid-prod-001', '2026-05-25', 6, 5, 6, 5, 7, 8, 4, TRUE),
(5, 'uuid-prod-009', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
(5, 'uuid-prod-011', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
(6, 'uuid-prod-003', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
(6, 'uuid-prod-013', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
(7, 'uuid-prod-005', '2026-06-01', 7, 5, 6, 5, 8, 9, 5, TRUE),
(7, 'uuid-prod-008', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
(8, 'uuid-prod-001', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
(8, 'uuid-prod-002', '2026-06-01', 2, 1, 2, 1, 3, 3, 1, TRUE),
(10, 'uuid-prod-009', '2026-06-08', 3, 2, 3, 2, 4, 5, 2, TRUE),
(10, 'uuid-prod-011', '2026-06-08', 4, 3, 4, 3, 5, 6, 3, TRUE);

-- 8. Reinsertar nota_venta_detalles con UUIDs
INSERT INTO nota_venta_detalles (cantidad, precio_venta, sub_total, producto_id, notaventa_id, activo) VALUES
(5, 26.00, 130.00, 'uuid-prod-009', 1, TRUE),
(10, 30.00, 300.00, 'uuid-prod-011', 1, TRUE),
(8, 22.00, 176.00, 'uuid-prod-010', 1, TRUE),
(10, 135.00, 1350.00, 'uuid-prod-003', 2, TRUE),
(8, 110.00, 880.00, 'uuid-prod-004', 2, TRUE),
(3, 62.00, 186.00, 'uuid-prod-012', 2, TRUE),
(12, 38.00, 456.00, 'uuid-prod-005', 3, TRUE),
(8, 28.00, 224.00, 'uuid-prod-007', 3, TRUE),
(6, 22.00, 132.00, 'uuid-prod-008', 3, TRUE),
(10, 22.00, 220.00, 'uuid-prod-001', 4, TRUE),
(4, 68.00, 272.00, 'uuid-prod-002', 4, TRUE),
(15, 26.00, 390.00, 'uuid-prod-009', 5, TRUE),
(10, 30.00, 300.00, 'uuid-prod-011', 5, TRUE),
(12, 135.00, 1620.00, 'uuid-prod-003', 6, TRUE),
(8, 110.00, 880.00, 'uuid-prod-004', 6, TRUE),
(10, 62.00, 620.00, 'uuid-prod-012', 6, TRUE),
(15, 38.00, 570.00, 'uuid-prod-005', 7, TRUE),
(8, 28.00, 224.00, 'uuid-prod-007', 7, TRUE),
(10, 22.00, 220.00, 'uuid-prod-001', 8, TRUE),
(5, 42.00, 210.00, 'uuid-prod-014', 9, TRUE);
