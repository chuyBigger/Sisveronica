-- ============================================
-- CATEGORIAS (10 registros con UUIDs)
-- ============================================
INSERT INTO categorias (id, nombre, partida, activo) VALUES
('b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Lácteos Básicos', 'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Quesos Finos', 'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Carnes Rojas', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Aves y Pollo', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Frutas Nacionales', 'FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'Verduras de Temporada', 'FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'Abarrotes Enlatados', 'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'Cereales y Granos', 'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'Embutidos', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'Limpieza', 'VARIOS', TRUE);

-- ============================================
-- CLIENTES (12 registros con UUIDs)
-- ============================================
INSERT INTO clientes (id, nombre, rfc, calle, numero, fraccionamiento, c_p, municipio, estado, activo) VALUES
('c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Restaurante El Sazón', 'RSA120101ABC', 'Hidalgo', 123, 'Centro', '20000', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Comedor La Familia', 'CLF850201DEF', 'Zaragoza', 45, 'San Marcos', '20100', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Hotel Real Inn', 'HRI900301GHI', 'Blvd. Universitario', 500, 'Zona Centro', '20200', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Taquería El Güero', 'TEG780401JKL', 'Av. Independencia', 78, 'Colonia Obraje', '20300', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Cocina Doña Mary', 'CDM650501MNO', 'Calle 5 de Mayo', 234, 'San Pedro', '20400', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567806', 'Pizzería Napoli', 'PNP920601PQR', 'Av. López Mateos', 156, 'Las Palmas', '20500', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567807', 'Fonda Lupita', 'FUL830701STU', 'Calle Juárez', 89, 'Centro', '20001', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567808', 'Cafetería Express', 'CAE950801VWX', 'Av. Aguascalientes', 345, 'Residencial', '20600', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567809', 'Club Deportivo Sport', 'CDS880901YZA', 'Circuito Deportivo', 12, 'San Luis', '20700', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567810', 'Hospital San José', 'HSJ771001BCD', 'Av. Salud', 789, 'Médico', '20800', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567811', 'Escuela Primaria Benito Juárez', 'EPB661101EFG', 'Calle Educación', 56, 'Escolar', '20900', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567812', 'Asilo Santa María', 'ASM551201HIJ', 'Av. Paz', 321, 'Tercera Edad', '21000', 'Aguascalientes', 'Aguascalientes', TRUE);

-- ============================================
-- PRODUCTOS (15 registros con UUIDs)
-- ============================================
INSERT INTO productos (id, codigo, nombre, partida, categoria_id, unidad_medida, precio_compra, precio_venta, activo) VALUES
('a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LEC-001', 'Leche Entera 1L', 'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LITRO', 16.50, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'QSO-002', 'Queso Panela 500g', 'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'KILO', 45.00, 68.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567803', 'CAR-003', 'Carne de Res Molida 1kg', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 95.00, 135.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567804', 'PLL-004', 'Pechuga de Pollo 1kg', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 78.00, 110.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'MAN-005', 'Manzana Roja 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO', 25.00, 38.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567806', 'PLA-006', 'Plátano Tabasco 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO', 12.00, 18.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'JIT-007', 'Jitomate Saladet 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO', 18.00, 28.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'CEB-008', 'Cebolla Blanca 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO', 14.00, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'FRJ-009', 'Frijol Refrito Lata 500g', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA', 18.00, 26.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'ATN-010', 'Atún en Agua Lata 150g', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA', 15.00, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'ARR-011', 'Arroz Blanco 1kg', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'KILO', 20.00, 30.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567812', 'SAL-012', 'Salchicha de Pollo 500g', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 42.00, 62.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567813', 'CHO-013', 'Chorizo de Res 500g', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 55.00, 80.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567814', 'JAB-014', 'Jabón Líquido Trastes 1L', 'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO', 28.00, 42.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567815', 'CLO-015', 'Cloro 1L', 'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO', 12.00, 18.00, TRUE);

-- ============================================
-- CONTRATOS (10 registros con UUIDs)
-- ============================================
INSERT INTO contratos (id, contrato, cliente_id, fecha_inicio, fecha_termino, presupuesto, activo) VALUES
('d1b2c3d4-e5f6-7890-abcd-ef1234567801', 'CON-001-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-01-01', '2026-06-30', 150000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CON-002-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', '2026-01-15', '2026-07-15', 85000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567803', 'CON-003-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', '2026-02-01', '2026-08-31', 200000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567804', 'CON-004-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', '2026-02-15', '2026-09-15', 60000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567805', 'CON-005-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-03-01', '2026-10-31', 95000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567806', 'CON-006-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567806', '2026-03-15', '2026-11-30', 110000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567807', 'CON-007-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567807', '2026-04-01', '2026-12-31', 75000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567808', 'CON-008-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567808', '2026-04-15', '2026-10-15', 50000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567809', 'CON-009-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-05-01', '2026-11-30', 130000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567810', 'CON-010-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567810', '2026-05-15', '2026-12-31', 180000.00, TRUE);

-- ============================================
-- ORDENES DE COMPRA (10 registros con UUIDs)
-- ============================================
INSERT INTO orden_compras (id, cliente_id, contrato_id, partida, fecha_inicio_semana, fecha_fin_semana, activo) VALUES
('e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', '2026-05-25', '2026-05-31', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES', '2026-05-25', '2026-05-31', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', '2026-05-25', '2026-05-31', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS', '2026-05-25', '2026-05-31', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'c1b2c3d4-e5f6-7890-abcd-ef1234567805', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'ABARROTES', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567806', 'c1b2c3d4-e5f6-7890-abcd-ef1234567806', 'd1b2c3d4-e5f6-7890-abcd-ef1234567806', 'CARNES', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567807', 'c1b2c3d4-e5f6-7890-abcd-ef1234567807', 'd1b2c3d4-e5f6-7890-abcd-ef1234567807', 'FRUTASYVERDURAS', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567808', 'c1b2c3d4-e5f6-7890-abcd-ef1234567808', 'd1b2c3d4-e5f6-7890-abcd-ef1234567808', 'LACTEOS', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567809', 'c1b2c3d4-e5f6-7890-abcd-ef1234567809', 'd1b2c3d4-e5f6-7890-abcd-ef1234567809', 'VARIOS', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567810', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', '2026-06-08', '2026-06-14', TRUE);

-- ============================================
-- ORDEN COMPRA DETALLES (20 registros con UUIDs)
-- ============================================
INSERT INTO orden_compra_detalles (id, orden_compra_id, producto_id, fecha, lunes, martes, miercoles, jueves, viernes, sabado, domingo, activo) VALUES
('e2b2c3d4-e5f6-7890-abcd-ef1234567810', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-05-25', 2, 3, 2, 3, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567811', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', '2026-05-25', 3, 2, 4, 2, 3, 4, 1, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567812', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', '2026-05-25', 5, 4, 5, 4, 6, 7, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567813', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567803', '2026-05-25', 4, 3, 5, 4, 6, 8, 4, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567814', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567804', '2026-05-25', 6, 5, 7, 5, 8, 10, 5, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567815', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567812', '2026-05-25', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567816', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-05-25', 8, 6, 7, 8, 10, 12, 6, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567817', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567806', '2026-05-25', 10, 8, 9, 10, 12, 15, 8, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567818', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', '2026-05-25', 5, 4, 6, 5, 7, 8, 4, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567819', 'e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-05-25', 6, 5, 6, 5, 7, 8, 4, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567820', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567821', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567822', 'e1b2c3d4-e5f6-7890-abcd-ef1234567806', 'a1b2c3d4-e5f6-7890-abcd-ef1234567803', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567823', 'e1b2c3d4-e5f6-7890-abcd-ef1234567806', 'a1b2c3d4-e5f6-7890-abcd-ef1234567813', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567824', 'e1b2c3d4-e5f6-7890-abcd-ef1234567807', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-06-01', 7, 5, 6, 5, 8, 9, 5, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567825', 'e1b2c3d4-e5f6-7890-abcd-ef1234567807', 'a1b2c3d4-e5f6-7890-abcd-ef1234567808', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567826', 'e1b2c3d4-e5f6-7890-abcd-ef1234567808', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567827', 'e1b2c3d4-e5f6-7890-abcd-ef1234567808', 'a1b2c3d4-e5f6-7890-abcd-ef1234567802', '2026-06-01', 2, 1, 2, 1, 3, 3, 1, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567828', 'e1b2c3d4-e5f6-7890-abcd-ef1234567810', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-08', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567829', 'e1b2c3d4-e5f6-7890-abcd-ef1234567810', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', '2026-06-08', 4, 3, 4, 3, 5, 6, 3, TRUE);

-- ============================================
-- NOTAS DE VENTA (10 registros con UUIDs)
-- ============================================
INSERT INTO nota_ventas (id, folio, fecha, cliente_id, contrato_id, partida, total_general, activo) VALUES
('f1b2c3d4-e5f6-7890-abcd-ef1234567801', 1001, '2026-05-25 08:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', 1250.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567802', 1002, '2026-05-25 10:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES', 2340.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567803', 1003, '2026-05-26 09:15:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', 890.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567804', 1004, '2026-05-26 11:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS', 650.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567805', 1005, '2026-05-27 08:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567805', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'ABARROTES', 1800.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567806', 1006, '2026-05-27 14:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567806', 'd1b2c3d4-e5f6-7890-abcd-ef1234567806', 'CARNES', 3200.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567807', 1007, '2026-05-28 07:45:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567807', 'd1b2c3d4-e5f6-7890-abcd-ef1234567807', 'FRUTASYVERDURAS', 1100.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567808', 1008, '2026-05-28 10:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567808', 'd1b2c3d4-e5f6-7890-abcd-ef1234567808', 'LACTEOS', 980.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567809', 1009, '2026-05-29 09:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567809', 'd1b2c3d4-e5f6-7890-abcd-ef1234567809', 'VARIOS', 450.00, TRUE),
('f1b2c3d4-e5f6-7890-abcd-ef1234567810', 1010, '2026-05-29 11:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', 1560.00, TRUE);

-- ============================================
-- NOTA VENTA DETALLES (20 registros con UUIDs)
-- ============================================
INSERT INTO nota_venta_detalles (id, cantidad, precio_venta, sub_total, producto_id, notaventa_id, activo) VALUES
('f2b2c3d4-e5f6-7890-abcd-ef1234567810', 5, 26.00, 130.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567811', 10, 30.00, 300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567812', 8, 22.00, 176.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567813', 10, 135.00, 1350.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567803', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567814', 8, 110.00, 880.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567804', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567815', 3, 62.00, 186.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567812', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567816', 12, 38.00, 456.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567817', 8, 28.00, 224.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567818', 6, 22.00, 132.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567819', 10, 22.00, 220.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567820', 4, 68.00, 272.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567821', 15, 26.00, 390.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567822', 10, 30.00, 300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567823', 12, 135.00, 1620.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567803', 'f1b2c3d4-e5f6-7890-abcd-ef1234567806', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567824', 8, 110.00, 880.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567804', 'f1b2c3d4-e5f6-7890-abcd-ef1234567806', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567825', 10, 62.00, 620.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567812', 'f1b2c3d4-e5f6-7890-abcd-ef1234567806', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567826', 15, 38.00, 570.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567807', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567827', 8, 28.00, 224.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'f1b2c3d4-e5f6-7890-abcd-ef1234567807', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567828', 10, 22.00, 220.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567808', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567829', 5, 42.00, 210.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567814', 'f1b2c3d4-e5f6-7890-abcd-ef1234567809', TRUE);
