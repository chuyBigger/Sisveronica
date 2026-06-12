CREATE TABLE nota_venta_detalles (
    id VARCHAR(36) PRIMARY KEY,
    cantidad INTEGER NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    sub_total DECIMAL(10,2) NOT NULL,
    notaventa_id VARCHAR(36) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_nvd_notaventa FOREIGN KEY (notaventa_id) REFERENCES nota_ventas(id),
    CONSTRAINT fk_nvd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

INSERT INTO nota_venta_detalles (id, cantidad, precio_venta, sub_total, producto_id, notaventa_id, activo) VALUES
-- Nota ABARROTES
('f2b2c3d4-e5f6-7890-abcd-ef1234567810', 5,  26.00, 130.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567811', 8,  22.00, 176.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567812', 10, 30.00, 300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', 'f1b2c3d4-e5f6-7890-abcd-ef1234567801', TRUE),
-- Nota CARNES
('f2b2c3d4-e5f6-7890-abcd-ef1234567813', 10, 130.00, 1300.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567814', 8,  98.00, 784.00,  'a1b2c3d4-e5f6-7890-abcd-ef1234567826', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567815', 3,  40.00, 120.00,  'a1b2c3d4-e5f6-7890-abcd-ef1234567842', 'f1b2c3d4-e5f6-7890-abcd-ef1234567802', TRUE),
-- Nota FRUTASYVERDURAS
('f2b2c3d4-e5f6-7890-abcd-ef1234567816', 12, 38.00, 456.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567817', 8,  28.00, 224.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567818', 6,  22.00, 132.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567808', 'f1b2c3d4-e5f6-7890-abcd-ef1234567803', TRUE),
-- Nota LACTEOS
('f2b2c3d4-e5f6-7890-abcd-ef1234567819', 10, 22.00, 220.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567820', 4,  68.00, 272.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'f1b2c3d4-e5f6-7890-abcd-ef1234567804', TRUE),
-- Nota GENERAL
('f2b2c3d4-e5f6-7890-abcd-ef1234567821', 5,  26.00, 130.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567822', 4,  130.00, 520.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567823', 3,  38.00, 114.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE),
('f2b2c3d4-e5f6-7890-abcd-ef1234567824', 6,  22.00, 132.00, 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'f1b2c3d4-e5f6-7890-abcd-ef1234567805', TRUE);
