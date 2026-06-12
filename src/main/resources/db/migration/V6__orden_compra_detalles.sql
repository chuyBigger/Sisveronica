CREATE TABLE orden_compra_detalles (
    id VARCHAR(36) PRIMARY KEY,
    orden_compra_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    fecha DATE NOT NULL,
    lunes DOUBLE,
    martes DOUBLE,
    miercoles DOUBLE,
    jueves DOUBLE,
    viernes DOUBLE,
    sabado DOUBLE,
    domingo DOUBLE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ocd_orden_compras FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id) ON DELETE CASCADE,
    CONSTRAINT fk_ocd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

INSERT INTO orden_compra_detalles (id, orden_compra_id, producto_id, fecha, lunes, martes, miercoles, jueves, viernes, sabado, domingo, activo) VALUES
-- ABARROTES: Frijol, Atún, Arroz
('e2b2c3d4-e5f6-7890-abcd-ef1234567810', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-01', 2, 3, 2, 3, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567811', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567810', '2026-06-01', 3, 2, 4, 2, 3, 4, 1, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567812', 'e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567811', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
-- CARNES: Pulpa Molida, Pechuga, Salchicha Cerdo
('e2b2c3d4-e5f6-7890-abcd-ef1234567813', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', '2026-06-01', 4, 3, 5, 4, 6, 8, 4, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567814', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567826', '2026-06-01', 6, 5, 7, 5, 8, 10, 5, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567815', 'e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567842', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
-- FRUTASYVERDURAS: Manzana, Plátano, Jitomate
('e2b2c3d4-e5f6-7890-abcd-ef1234567816', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-06-01', 8, 6, 7, 8, 10, 12, 6, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567817', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567806', '2026-06-01', 10, 8, 9, 10, 12, 15, 8, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567818', 'e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567807', '2026-06-01', 5, 4, 6, 5, 7, 8, 4, TRUE),
-- LACTEOS: Leche
('e2b2c3d4-e5f6-7890-abcd-ef1234567819', 'e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-06-01', 6, 5, 6, 5, 7, 8, 4, TRUE),
-- GENERAL: Frijol, Pulpa Molida, Manzana, Leche
('e2b2c3d4-e5f6-7890-abcd-ef1234567820', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567809', '2026-06-01', 3, 2, 3, 2, 4, 5, 2, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567821', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567825', '2026-06-01', 4, 3, 4, 3, 5, 6, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567822', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-06-01', 5, 4, 5, 4, 6, 7, 3, TRUE),
('e2b2c3d4-e5f6-7890-abcd-ef1234567823', 'e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-06-01', 2, 1, 2, 1, 3, 3, 1, TRUE);
