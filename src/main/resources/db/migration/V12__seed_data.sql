-- ============================================
-- CATEGORIAS
-- ============================================
INSERT INTO categorias (id, nombre, partida, activo) VALUES
('b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Lácteos Básicos', 'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Quesos Finos', 'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Carnes de Res', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Aves y Pollo', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Frutas Nacionales', 'FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'Verduras de Temporada', 'FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'Abarrotes Enlatados', 'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'Cereales y Granos', 'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'Embutidos y Procesados', 'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'Limpieza', 'VARIOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'Carnes de Cerdo', 'CARNES', TRUE);

-- ============================================
-- CLIENTES (incluye "General" para notas/órdenes de partida GENERAL)
-- ============================================
INSERT INTO clientes (id, nombre, rfc, calle, numero, fraccionamiento, c_p, municipio, estado, activo) VALUES
('c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Restaurante El Sazón', 'RSA120101ABC', 'Hidalgo', 123, 'Centro', '20000', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Comedor La Familia', 'CLF850201DEF', 'Zaragoza', 45, 'San Marcos', '20100', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Hotel Real Inn', 'HRI900301GHI', 'Blvd. Universitario', 500, 'Zona Centro', '20200', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Taquería El Güero', 'TEG780401JKL', 'Av. Independencia', 78, 'Colonia Obraje', '20300', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Cocina Doña Mary', 'CDM650501MNO', 'Calle 5 de Mayo', 234, 'San Pedro', '20400', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'General', 'GENERAL000000', 'N/A', 0, 'N/A', '00000', 'Aguascalientes', 'Aguascalientes', TRUE);

-- ============================================
-- PRODUCTOS — CARNES: 25 productos oficiales del cliente
-- ============================================
INSERT INTO productos (id, codigo, nombre, partida, categoria_id, unidad_medida, precio_compra, precio_venta, activo) VALUES
-- LACTEOS
('a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LEC-001', 'Leche Entera 1L', 'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LITRO', 16.50, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'QSO-002', 'Queso Panela 500g', 'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'KILO', 45.00, 68.00, TRUE),
-- CARNES DE RES (b1...03)
('a1b2c3d4-e5f6-7890-abcd-ef1234567820', 'RES-020', 'Bistec de Pierna de Res', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 110.00, 160.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567821', 'RES-021', 'Chambape de Res', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567822', 'RES-022', 'Cuate de Pierna de Res', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567823', 'RES-023', 'Falda de Res', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 95.00, 140.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567824', 'RES-024', 'Pulpa de Res en Trozo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 98.00, 145.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'RES-025', 'Pulpa de Res Molida', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 88.00, 130.00, TRUE),
-- AVES Y POLLO (b1...04)
('a1b2c3d4-e5f6-7890-abcd-ef1234567826', 'POL-026', 'Pechuga de Pollo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 65.00, 98.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567827', 'POL-027', 'Pechuga de Pollo Deshuesada', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 100.00, 150.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567828', 'POL-028', 'Pechuga Deshuesada en Bistec', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 100.00, 150.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567829', 'POL-029', 'Pechuga de Pollo Molida', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 50.00, 75.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567830', 'POL-030', 'Pierna y Muslo de Pollo sin Piel Pistola', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 43.00, 65.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567831', 'POL-031', 'Pollo Entero', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 33.00, 50.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567838', 'AVE-038', 'Huevo Entero', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO', 25.00, 38.00, TRUE),
-- CARNES DE CERDO (b1...11)
('a1b2c3d4-e5f6-7890-abcd-ef1234567832', 'CER-032', 'Chuleta de Cerdo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 60.00, 89.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567833', 'CER-033', 'Lomo de Cerdo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 65.00, 95.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567834', 'CER-034', 'Pierna de Cerdo en Trozo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 55.00, 82.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567835', 'CER-035', 'Pierna de Cerdo Molida', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 40.00, 60.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567836', 'CER-036', 'Jamón de Cerdo Fino 16% Proteína', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 125.00, 180.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567837', 'CER-037', 'Tocino', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO', 95.00, 140.00, TRUE),
-- EMBUTIDOS Y PROCESADOS (b1...09)
('a1b2c3d4-e5f6-7890-abcd-ef1234567839', 'EMB-039', 'Jamón de Pavo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567840', 'EMB-040', 'Jamón de Pechuga de Pavo', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 95.00, 140.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567841', 'EMB-041', 'Mortadela', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 27.00, 40.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567842', 'EMB-042', 'Salchicha de Cerdo Viena', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 27.00, 40.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567843', 'EMB-043', 'Salchicha de Pavo Viena', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO', 33.00, 50.00, TRUE),
-- OTROS CARNES
('a1b2c3d4-e5f6-7890-abcd-ef1234567844', 'MER-044', 'Mero en Filete Congelado', 'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO', 60.00, 90.00, TRUE),
-- FRUTASYVERDURAS
('a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'MAN-005', 'Manzana Roja 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO', 25.00, 38.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567806', 'PLA-006', 'Plátano Tabasco 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO', 12.00, 18.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'JIT-007', 'Jitomate Saladet 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO', 18.00, 28.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'CEB-008', 'Cebolla Blanca 1kg', 'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO', 14.00, 22.00, TRUE),
-- ABARROTES
('a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'FRJ-009', 'Frijol Refrito Lata 500g', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA', 18.00, 26.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'ATN-010', 'Atún en Agua Lata 150g', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA', 15.00, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'ARR-011', 'Arroz Blanco 1kg', 'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'KILO', 20.00, 30.00, TRUE),
-- VARIOS
('a1b2c3d4-e5f6-7890-abcd-ef1234567814', 'JAB-014', 'Jabón Líquido Trastes 1L', 'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO', 28.00, 42.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567815', 'CLO-015', 'Cloro 1L', 'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO', 12.00, 18.00, TRUE);

-- ============================================
-- CONTRATOS
-- ============================================
INSERT INTO contratos (id, contrato, cliente_id, fecha_inicio, fecha_termino, presupuesto, activo) VALUES
('d1b2c3d4-e5f6-7890-abcd-ef1234567801', 'CON-001-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-01-01', '2026-06-30', 150000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CON-002-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', '2026-01-15', '2026-07-15', 85000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567803', 'CON-003-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', '2026-02-01', '2026-08-31', 200000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567804', 'CON-004-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', '2026-02-15', '2026-09-15', 60000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567805', 'CON-005-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-03-01', '2026-10-31', 95000.00, TRUE);

-- ============================================
-- ORDENES DE COMPRA — cada una con productos de SU partida
-- ============================================
INSERT INTO orden_compras (id, cliente_id, contrato_id, partida, fecha_inicio_semana, fecha_fin_semana, activo) VALUES
('e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES',  '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES',     '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS',    '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'GENERAL',    '2026-06-01', '2026-06-07', TRUE);

-- ============================================
-- ORDEN COMPRA DETALLES — cada producto es de la partida de SU orden
-- ============================================
INSERT INTO orden_compra_detalles (id, orden_compra_id, producto_id, fecha, lunes, martes, miercoles, jueves, viernes, sabado, domingo, activo) VALUES
-- Orden ABARROTES: productos ABARROTES
('e2b2c3d4-e5f6-7890-abcd-ef1234567810', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-01', 2, 3, 2, 3, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567811', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', '2026-06-01', 3, 2, 4, 2, 3, 4, 1, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567812', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
-- Orden CARNES: productos CARNES nuevos
('e2b2c3d4-e5f6-7890-abcd-ef1234567813', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', '2026-06-01', 4, 3, 5, 4, 6, 8, 4, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567814', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567826', '2026-06-01', 6, 5, 7, 5, 8, 10, 5, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567815', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567842', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
-- Orden FRUTASYVERDURAS: productos FRUTASYVERDURAS
('e2b2c3d4-e5f6-7890-abcd-ef1234567816', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-06-01', 8, 6, 7, 8, 10, 12, 6, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567817', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567806', '2026-06-01', 10, 8, 9, 10, 12, 15, 8, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567818', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', '2026-06-01', 5, 4, 6, 5, 7, 8, 4, TRUE),
-- Orden LACTEOS: productos LACTEOS
('e2b2c3d4-e5f6-7890-abcd-ef1234567819', 'e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-06-01', 6, 5, 6, 5, 7, 8, 4, TRUE),
-- Orden GENERAL: productos de varias partidas
('e2b2c3d4-e5f6-7890-abcd-ef1234567820', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567821', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567822', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567823', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-06-01', 2, 1, 2, 1, 3, 3, 1, TRUE);

-- ============================================
-- NOTAS DE VENTA — cada una con productos de SU partida
-- ============================================
INSERT INTO nota_ventas (id, folio, fecha, cliente_id, contrato_id, partida, total_general, activo) VALUES
-- ABARROTES: Frijol(26)×5=130 + Atún(22)×8=176 + Arroz(30)×10=300 = 606
('f1b2c3d4-e5f6-7890-abcd-ef1234567801', 1001, '2026-06-01 08:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', 606.00, TRUE),
-- CARNES: Pulpa Res Molida(130)×10=1300 + Pechuga Pollo(98)×8=784 + Salchicha Cerdo(40)×3=120 = 2204
('f1b2c3d4-e5f6-7890-abcd-ef1234567802', 1002, '2026-06-01 10:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES', 2204.00, TRUE),
-- FRUTASYVERDURAS: Manzana(38)×12=456 + Jitomate(28)×8=224 + Cebolla(22)×6=132 = 812
('f1b2c3d4-e5f6-7890-abcd-ef1234567803', 1003, '2026-06-02 09:15:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', 812.00, TRUE),
-- LACTEOS: Leche(22)×10=220 + Queso(68)×4=272 = 492
('f1b2c3d4-e5f6-7890-abcd-ef1234567804', 1004, '2026-06-02 11:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS', 492.00, TRUE),
-- GENERAL: Frijol(26)×5=130 + Pulpa Res Molida(130)×4=520 + Manzana(38)×3=114 + Leche(22)×6=132 = 896
('f1b2c3d4-e5f6-7890-abcd-ef1234567805', 1005, '2026-06-03 08:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'GENERAL', 896.00, TRUE);

-- ============================================
-- NOTA VENTA DETALLES — cada producto es de la partida de SU nota
-- ============================================
INSERT INTO nota_venta_detalles (id, cantidad, precio_venta, sub_total, producto_id, notaventa_id, activo) VALUES
-- Nota ABARROTES: productos ABARROTES
('f2b2c3d4-e5f6-7890-abcd-ef1234567810', 5,  26.00, 130.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567811', 8,  22.00, 176.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567812', 10, 30.00, 300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
-- Nota CARNES: productos CARNES nuevos
('f2b2c3d4-e5f6-7890-abcd-ef1234567813', 10, 130.00, 1300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567814', 8,  98.00, 784.00,  'a1b2c3d4-e5f6-7890-abcd-ef1234567826', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567815', 3,  40.00, 120.00,  'a1b2c3d4-e5f6-7890-abcd-ef1234567842', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
-- Nota FRUTASYVERDURAS: productos FRUTASYVERDURAS
('f2b2c3d4-e5f6-7890-abcd-ef1234567816', 12, 38.00, 456.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567817', 8,  28.00, 224.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567818', 6,  22.00, 132.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
-- Nota LACTEOS: productos LACTEOS
('f2b2c3d4-e5f6-7890-abcd-ef1234567819', 10, 22.00, 220.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567820', 4,  68.00, 272.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
-- Nota GENERAL: productos de varias partidas
('f2b2c3d4-e5f6-7890-abcd-ef1234567821', 5,  26.00, 130.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567822', 4,  130.00, 520.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567823', 3,  38.00, 114.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567824', 6,  22.00, 132.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE);
