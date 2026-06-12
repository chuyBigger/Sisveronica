CREATE TABLE extras (
    id VARCHAR(36) PRIMARY KEY,
    orden_compra_id VARCHAR(36) NOT NULL,
    dia VARCHAR(10) NOT NULL,
    fecha DATE NOT NULL,
    folio INT NOT NULL,
    firmada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion DATETIME,
    creado_por VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_extras_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);
