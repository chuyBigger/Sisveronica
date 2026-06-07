CREATE TABLE facturas (
    id VARCHAR(36) PRIMARY KEY,
    folio INT NOT NULL,
    orden_compra_id VARCHAR(36) NOT NULL,
    cliente VARCHAR(255) NOT NULL,
    contrato VARCHAR(255),
    partida VARCHAR(100) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    total_general DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_fact_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);
