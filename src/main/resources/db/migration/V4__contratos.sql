CREATE TABLE contratos (
    id VARCHAR(36) PRIMARY KEY,
    contrato VARCHAR(100) NOT NULL UNIQUE,
    cliente_id VARCHAR(36) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    presupuesto DECIMAL(12,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_contratos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

INSERT INTO contratos (id, contrato, cliente_id, fecha_inicio, fecha_termino, presupuesto, activo) VALUES
('d1b2c3d4-e5f6-7890-abcd-ef1234567801', 'CON-001-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567801', '2026-01-01', '2026-06-30', 150000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567802', 'CON-002-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567802', '2026-01-15', '2026-07-15', 85000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567803', 'CON-003-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567803', '2026-02-01', '2026-08-31', 200000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567804', 'CON-004-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567804', '2026-02-15', '2026-09-15', 60000.00, TRUE),
('d1b2c3d4-e5f6-7890-abcd-ef1234567805', 'CON-005-2026', 'c1b2c3d4-e5f6-7890-abcd-ef1234567805', '2026-03-01', '2026-10-31', 95000.00, TRUE);
