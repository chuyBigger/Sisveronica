CREATE TABLE productos (
    id VARCHAR(36) PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    partida VARCHAR(50) NOT NULL,
    categoria_id VARCHAR(36),
    unidad_medida VARCHAR(50) NOT NULL,
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

INSERT INTO productos (id, codigo, nombre, partida, categoria_id, unidad_medida, precio_compra, precio_venta, activo) VALUES
-- LACTEOS
('a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LEC-001', 'Leche Entera 1L',            'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'LITRO',  16.50, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'QSO-002', 'Queso Panela 500g',          'LACTEOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'KILO',   45.00, 68.00, TRUE),
-- CARNES DE RES
('a1b2c3d4-e5f6-7890-abcd-ef1234567820', 'RES-020', 'Bistec de Pierna de Res',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   110.00, 160.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567821', 'RES-021', 'Chambarete de Res',          'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567822', 'RES-022', 'Cuete de Pierna de Res',     'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567823', 'RES-023', 'Falda de Res',               'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   95.00, 140.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567824', 'RES-024', 'Pulpa de Res en Trozo',      'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   98.00, 145.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'RES-025', 'Pulpa de Res Molida',        'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   88.00, 130.00, TRUE),
-- AVES Y POLLO
('a1b2c3d4-e5f6-7890-abcd-ef1234567826', 'POL-026', 'Pechuga de Pollo',            'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   65.00, 98.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567827', 'POL-027', 'Pechuga de Pollo Deshuesada','CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   100.00, 150.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567828', 'POL-028', 'Pechuga Desh. en Bistec',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   100.00, 150.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567829', 'POL-029', 'Pechuga de Pollo Molida',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   50.00, 75.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567830', 'POL-030', 'Pierna y Muslo sin Piel',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   43.00, 65.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567831', 'POL-031', 'Pollo Entero',               'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   33.00, 50.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567838', 'AVE-038', 'Huevo Entero',               'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'KILO',   25.00, 38.00, TRUE),
-- CARNES DE CERDO
('a1b2c3d4-e5f6-7890-abcd-ef1234567832', 'CER-032', 'Chuleta de Cerdo',           'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   60.00, 89.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567833', 'CER-033', 'Lomo de Cerdo',              'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   65.00, 95.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567834', 'CER-034', 'Pierna de Cerdo en Trozo',   'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   55.00, 82.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567835', 'CER-035', 'Pierna de Cerdo Molida',     'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   40.00, 60.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567836', 'CER-036', 'Jamón de Cerdo Fino 16%',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   125.00, 180.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567837', 'CER-037', 'Tocino',                     'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'KILO',   95.00, 140.00, TRUE),
-- EMBUTIDOS
('a1b2c3d4-e5f6-7890-abcd-ef1234567839', 'EMB-039', 'Jamón de Pavo',              'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO',   82.00, 120.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567840', 'EMB-040', 'Jamón de Pechuga de Pavo',   'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO',   95.00, 140.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567841', 'EMB-041', 'Mortadela',                  'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO',   27.00, 40.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567842', 'EMB-042', 'Salchicha de Cerdo Viena',   'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO',   27.00, 40.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567843', 'EMB-043', 'Salchicha de Pavo Viena',    'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'KILO',   33.00, 50.00, TRUE),
-- OTROS
('a1b2c3d4-e5f6-7890-abcd-ef1234567844', 'MER-044', 'Mero en Filete Congelado',   'CARNES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'KILO',   60.00, 90.00, TRUE),
-- FRUTASYVERDURAS
('a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'MAN-005', 'Manzana Roja 1kg',           'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO',   25.00, 38.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567806', 'PLA-006', 'Plátano Tabasco 1kg',        'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'KILO',   12.00, 18.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'JIT-007', 'Jitomate Saladet 1kg',       'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO',   18.00, 28.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'CEB-008', 'Cebolla Blanca 1kg',         'FRUTASYVERDURAS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'KILO',   14.00, 22.00, TRUE),
-- ABARROTES
('a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'FRJ-009', 'Frijol Refrito Lata 500g',   'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA',  18.00, 26.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'ATN-010', 'Atún en Agua Lata 150g',     'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'PIEZA',  15.00, 22.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'ARR-011', 'Arroz Blanco 1kg',           'ABARROTES', 'b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'KILO',   20.00, 30.00, TRUE),
-- VARIOS
('a1b2c3d4-e5f6-7890-abcd-ef1234567814', 'JAB-014', 'Jabón Líquido Trastes 1L',   'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO',   28.00, 42.00, TRUE),
('a1b2c3d4-e5f6-7890-abcd-ef1234567815', 'CLO-015', 'Cloro 1L',                   'VARIOS', 'b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'LITRO',   12.00, 18.00, TRUE);
