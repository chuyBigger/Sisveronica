-- ============================================
-- CATEGORIAS (10 registros)
-- ============================================
INSERT INTO categorias (nombre, partida, activo) VALUES
('Lácteos Básicos', 'LACTEOS', TRUE),
('Quesos Finos', 'LACTEOS', TRUE),
('Carnes Rojas', 'CARNES', TRUE),
('Aves y Pollo', 'CARNES', TRUE),
('Frutas Nacionales', 'FRUTASYVERDURAS', TRUE),
('Verduras de Temporada', 'FRUTASYVERDURAS', TRUE),
('Abarrotes Enlatados', 'ABARROTES', TRUE),
('Cereales y Granos', 'ABARROTES', TRUE),
('Embutidos', 'CARNES', TRUE),
('Limpieza', 'VARIOS', TRUE);

-- ============================================
-- CLIENTES (12 registros)
-- ============================================
INSERT INTO clientes (nombre, rfc, calle, numero, fraccionamiento, c_p, municipio, estado, activo) VALUES
('Restaurante El Sazón', 'RSA120101ABC', 'Hidalgo', 123, 'Centro', '20000', 'Aguascalientes', 'Aguascalientes', TRUE),
('Comedor La Familia', 'CLF850201DEF', 'Zaragoza', 45, 'San Marcos', '20100', 'Aguascalientes', 'Aguascalientes', TRUE),
('Hotel Real Inn', 'HRI900301GHI', 'Blvd. Universitario', 500, 'Zona Centro', '20200', 'Aguascalientes', 'Aguascalientes', TRUE),
('Taquería El Güero', 'TEG780401JKL', 'Av. Independencia', 78, 'Colonia Obraje', '20300', 'Aguascalientes', 'Aguascalientes', TRUE),
('Cocina Doña Mary', 'CDM650501MNO', 'Calle 5 de Mayo', 234, 'San Pedro', '20400', 'Aguascalientes', 'Aguascalientes', TRUE),
('Pizzería Napoli', 'PNP920601PQR', 'Av. López Mateos', 156, 'Las Palmas', '20500', 'Aguascalientes', 'Aguascalientes', TRUE),
('Fonda Lupita', 'FUL830701STU', 'Calle Juárez', 89, 'Centro', '20001', 'Aguascalientes', 'Aguascalientes', TRUE),
('Cafetería Express', 'CAE950801VWX', 'Av. Aguascalientes', 345, 'Residencial', '20600', 'Aguascalientes', 'Aguascalientes', TRUE),
('Club Deportivo Sport', 'CDS880901YZA', 'Circuito Deportivo', 12, 'San Luis', '20700', 'Aguascalientes', 'Aguascalientes', TRUE),
('Hospital San José', 'HSJ771001BCD', 'Av. Salud', 789, 'Médico', '20800', 'Aguascalientes', 'Aguascalientes', TRUE),
('Escuela Primaria Benito Juárez', 'EPB661101EFG', 'Calle Educación', 56, 'Escolar', '20900', 'Aguascalientes', 'Aguascalientes', TRUE),
('Asilo Santa María', 'ASM551201HIJ', 'Av. Paz', 321, 'Tercera Edad', '21000', 'Aguascalientes', 'Aguascalientes', TRUE);

-- ============================================
-- PRODUCTOS (15 registros)
-- ============================================
INSERT INTO productos (nombre, partida, categoria_id, unidad_medida, precio_compra, precio_venta, activo) VALUES
('Leche Entera 1L', 'LACTEOS', 1, 'LITRO', 16.50, 22.00, TRUE),
('Queso Panela 500g', 'LACTEOS', 2, 'KILO', 45.00, 68.00, TRUE),
('Carne de Res Molida 1kg', 'CARNES', 3, 'KILO', 95.00, 135.00, TRUE),
('Pechuga de Pollo 1kg', 'CARNES', 4, 'KILO', 78.00, 110.00, TRUE),
('Manzana Roja 1kg', 'FRUTASYVERDURAS', 5, 'KILO', 25.00, 38.00, TRUE),
('Plátano Tabasco 1kg', 'FRUTASYVERDURAS', 5, 'KILO', 12.00, 18.00, TRUE),
('Jitomate Saladet 1kg', 'FRUTASYVERDURAS', 6, 'KILO', 18.00, 28.00, TRUE),
('Cebolla Blanca 1kg', 'FRUTASYVERDURAS', 6, 'KILO', 14.00, 22.00, TRUE),
('Frijol Refrito Lata 500g', 'ABARROTES', 7, 'PIEZA', 18.00, 26.00, TRUE),
('Atún en Agua Lata 150g', 'ABARROTES', 7, 'PIEZA', 15.00, 22.00, TRUE),
('Arroz Blanco 1kg', 'ABARROTES', 8, 'KILO', 20.00, 30.00, TRUE),
('Salchicha de Pollo 500g', 'CARNES', 9, 'KILO', 42.00, 62.00, TRUE),
('Chorizo de Res 500g', 'CARNES', 9, 'KILO', 55.00, 80.00, TRUE),
('Jabón Líquido Trastes 1L', 'VARIOS', 10, 'LITRO', 28.00, 42.00, TRUE),
('Cloro 1L', 'VARIOS', 10, 'LITRO', 12.00, 18.00, TRUE);

-- ============================================
-- CONTRATOS (10 registros)
-- ============================================
INSERT INTO contratos (contrato, cliente_id, fecha_inicio, fecha_termino, presupuesto, activo) VALUES
('CON-001-2026', 1, '2026-01-01', '2026-06-30', 150000.00, TRUE),
('CON-002-2026', 2, '2026-01-15', '2026-07-15', 85000.00, TRUE),
('CON-003-2026', 3, '2026-02-01', '2026-08-31', 200000.00, TRUE),
('CON-004-2026', 4, '2026-02-15', '2026-09-15', 60000.00, TRUE),
('CON-005-2026', 5, '2026-03-01', '2026-10-31', 95000.00, TRUE),
('CON-006-2026', 6, '2026-03-15', '2026-11-30', 110000.00, TRUE),
('CON-007-2026', 7, '2026-04-01', '2026-12-31', 75000.00, TRUE),
('CON-008-2026', 8, '2026-04-15', '2026-10-15', 50000.00, TRUE),
('CON-009-2026', 9, '2026-05-01', '2026-11-30', 130000.00, TRUE),
('CON-010-2026', 10, '2026-05-15', '2026-12-31', 180000.00, TRUE);

-- ============================================
-- ORDENES DE COMPRA (10 registros)
-- ============================================
INSERT INTO orden_compras (cliente_id, contrato_id, partida, fecha_inicio_semana, fecha_fin_semana, activo) VALUES
(1, 1, 'ABARROTES', '2026-05-25', '2026-05-31', TRUE),
(2, 2, 'CARNES', '2026-05-25', '2026-05-31', TRUE),
(3, 3, 'FRUTASYVERDURAS', '2026-05-25', '2026-05-31', TRUE),
(4, 4, 'LACTEOS', '2026-05-25', '2026-05-31', TRUE),
(5, 5, 'ABARROTES', '2026-06-01', '2026-06-07', TRUE),
(6, 6, 'CARNES', '2026-06-01', '2026-06-07', TRUE),
(7, 7, 'FRUTASYVERDURAS', '2026-06-01', '2026-06-07', TRUE),
(8, 8, 'LACTEOS', '2026-06-01', '2026-06-07', TRUE),
(9, 9, 'VARIOS', '2026-06-01', '2026-06-07', TRUE),
(1, 1, 'ABARROTES', '2026-06-08', '2026-06-14', TRUE);

-- ============================================
-- ORDEN COMPRA DETALLES (20 registros)
-- ============================================
INSERT INTO orden_compra_detalles (orden_compra_id, fecha, producto_id, lunes, martes, miercoles, jueves, viernes, sabado, domingo, activo) VALUES
(1, '2026-05-25', 9, 2, 3, 2, 3, 4, 5, 2, TRUE),
(1, '2026-05-25', 10, 3, 2, 4, 2, 3, 4, 1, TRUE),
(1, '2026-05-25', 11, 5, 4, 5, 4, 6, 7, 3, TRUE),
(2, '2026-05-25', 3, 4, 3, 5, 4, 6, 8, 4, TRUE),
(2, '2026-05-25', 4, 6, 5, 7, 5, 8, 10, 5, TRUE),
(2, '2026-05-25', 12, 3, 2, 3, 2, 4, 5, 2, TRUE),
(3, '2026-05-25', 5, 8, 6, 7, 8, 10, 12, 6, TRUE),
(3, '2026-05-25', 6, 10, 8, 9, 10, 12, 15, 8, TRUE),
(3, '2026-05-25', 7, 5, 4, 6, 5, 7, 8, 4, TRUE),
(4, '2026-05-25', 1, 6, 5, 6, 5, 7, 8, 4, TRUE),
(5, '2026-06-01', 9, 3, 2, 3, 2, 4, 5, 2, TRUE),
(5, '2026-06-01', 11, 4, 3, 4, 3, 5, 6, 3, TRUE),
(6, '2026-06-01', 3, 5, 4, 5, 4, 6, 7, 3, TRUE),
(6, '2026-06-01', 13, 3, 2, 3, 2, 4, 5, 2, TRUE),
(7, '2026-06-01', 5, 7, 5, 6, 5, 8, 9, 5, TRUE),
(7, '2026-06-01', 8, 4, 3, 4, 3, 5, 6, 3, TRUE),
(8, '2026-06-01', 1, 5, 4, 5, 4, 6, 7, 3, TRUE),
(8, '2026-06-01', 2, 2, 1, 2, 1, 3, 3, 1, TRUE),
(10, '2026-06-08', 9, 3, 2, 3, 2, 4, 5, 2, TRUE),
(10, '2026-06-08', 11, 4, 3, 4, 3, 5, 6, 3, TRUE);

-- ============================================
-- NOTAS DE VENTA (10 registros)
-- ============================================
INSERT INTO nota_ventas (folio, fecha, cliente_id, contrato_id, partida, total_general, activo) VALUES
(1001, '2026-05-25 08:30:00', 1, 1, 'ABARROTES', 1250.00, TRUE),
(1002, '2026-05-25 10:00:00', 2, 2, 'CARNES', 2340.00, TRUE),
(1003, '2026-05-26 09:15:00', 3, 3, 'FRUTASYVERDURAS', 890.00, TRUE),
(1004, '2026-05-26 11:30:00', 4, 4, 'LACTEOS', 650.00, TRUE),
(1005, '2026-05-27 08:00:00', 5, 5, 'ABARROTES', 1800.00, TRUE),
(1006, '2026-05-27 14:00:00', 6, 6, 'CARNES', 3200.00, TRUE),
(1007, '2026-05-28 07:45:00', 7, 7, 'FRUTASYVERDURAS', 1100.00, TRUE),
(1008, '2026-05-28 10:30:00', 8, 8, 'LACTEOS', 980.00, TRUE),
(1009, '2026-05-29 09:00:00', 9, 9, 'VARIOS', 450.00, TRUE),
(1010, '2026-05-29 11:00:00', 1, 1, 'ABARROTES', 1560.00, TRUE);

-- ============================================
-- NOTA VENTA DETALLES (20 registros)
-- ============================================
INSERT INTO nota_venta_detalles (cantidad, precio_venta, sub_total, producto_id, notaventa_id, activo) VALUES
(5, 26.00, 130.00, 9, 1, TRUE),
(10, 30.00, 300.00, 11, 1, TRUE),
(8, 22.00, 176.00, 10, 1, TRUE),
(10, 135.00, 1350.00, 3, 2, TRUE),
(8, 110.00, 880.00, 4, 2, TRUE),
(3, 62.00, 186.00, 12, 2, TRUE),
(12, 38.00, 456.00, 5, 3, TRUE),
(8, 28.00, 224.00, 7, 3, TRUE),
(6, 22.00, 132.00, 8, 3, TRUE),
(10, 22.00, 220.00, 1, 4, TRUE),
(4, 68.00, 272.00, 2, 4, TRUE),
(15, 26.00, 390.00, 9, 5, TRUE),
(10, 30.00, 300.00, 11, 5, TRUE),
(12, 135.00, 1620.00, 3, 6, TRUE),
(8, 110.00, 880.00, 4, 6, TRUE),
(10, 62.00, 620.00, 12, 6, TRUE),
(15, 38.00, 570.00, 5, 7, TRUE),
(8, 28.00, 224.00, 7, 7, TRUE),
(10, 22.00, 220.00, 1, 8, TRUE),
(5, 42.00, 210.00, 14, 9, TRUE);
