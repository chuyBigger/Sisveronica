CREATE TABLE orden_compras (
    id VARCHAR(36) PRIMARY KEY,
    cliente_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36) NOT NULL,
    partida VARCHAR(100) NOT NULL,
    fecha_inicio_semana DATE NOT NULL,
    fecha_fin_semana DATE NOT NULL,
    confirmado_por VARCHAR(100) DEFAULT NULL,
    fecha_confirmacion DATETIME DEFAULT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_orden_compra_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_orden_compra_contrato FOREIGN KEY (contrato_id) REFERENCES contratos(id),
    CONSTRAINT uk_orden_compra_cliente_partida_semana UNIQUE (cliente_id, partida, fecha_inicio_semana)
);

INSERT INTO orden_compras (id, cliente_id, contrato_id, partida, fecha_inicio_semana, fecha_fin_semana, activo) VALUES
('e1b2c3d4-e5f6-7890-abcd-ef1234567801', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'd1b2c3d4-e5f6-7890-abcd-ef1234567801', 'ABARROTES',       '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567802', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'd1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CARNES',          '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567803', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'd1b2c3d4-e5f6-7890-abcd-ef1234567803', 'FRUTASYVERDURAS', '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567804', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'd1b2c3d4-e5f6-7890-abcd-ef1234567804', 'LACTEOS',         '2026-06-01', '2026-06-07', TRUE),
('e1b2c3d4-e5f6-7890-abcd-ef1234567805', 'c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'd1b2c3d4-e5f6-7890-abcd-ef1234567805', 'GENERAL',         '2026-06-01', '2026-06-07', TRUE);
