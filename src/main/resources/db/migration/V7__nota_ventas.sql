CREATE TABLE nota_ventas (
    id VARCHAR(36) PRIMARY KEY,
    folio INT UNIQUE,
    fecha DATETIME NOT NULL,
    cliente_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36),
    orden_compra_id VARCHAR(36),
    partida VARCHAR(100) NOT NULL,
    dia VARCHAR(10) DEFAULT NULL,
    firmada BOOLEAN NOT NULL DEFAULT FALSE,
    detalle TEXT NULL,
    total_general DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_notaventa_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_notaventa_contrato FOREIGN KEY (contrato_id) REFERENCES contratos(id),
    CONSTRAINT fk_notaventa_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);

INSERT INTO nota_ventas (id, folio, fecha, cliente_id, contrato_id, partida, total_general, activo) VALUES
-- ABARROTES
('f1b2c3d4-e5f6-7890-abcd-ef1234567801', 1001, '2026-06-01 08:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES', 606.00, TRUE),
-- CARNES
('f1b2c3d4-e5f6-7890-abcd-ef1234567802', 1002, '2026-06-01 10:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES', 2204.00, TRUE),
-- FRUTASYVERDURAS
('f1b2c3d4-e5f6-7890-abcd-ef1234567803', 1003, '2026-06-02 09:15:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', 812.00, TRUE),
-- LACTEOS
('f1b2c3d4-e5f6-7890-abcd-ef1234567804', 1004, '2026-06-02 11:30:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS', 492.00, TRUE),
-- GENERAL
('f1b2c3d4-e5f6-7890-abcd-ef1234567805', 1005, '2026-06-03 08:00:00', 'c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'GENERAL', 896.00, TRUE);
