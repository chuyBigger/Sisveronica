CREATE TABLE nota_cancelaciones (
    id VARCHAR(36) PRIMARY KEY,
    orden_compra_id VARCHAR(36) NOT NULL,
    dia VARCHAR(10) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    creado_por VARCHAR(100),
    validado_por VARCHAR(100),
    fecha_validacion DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_nc_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);
