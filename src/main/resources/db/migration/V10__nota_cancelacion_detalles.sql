CREATE TABLE nota_cancelacion_detalles (
    id VARCHAR(36) PRIMARY KEY,
    nota_cancelacion_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    cantidad_cancelada DOUBLE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ncd_nota_cancelacion FOREIGN KEY (nota_cancelacion_id) REFERENCES nota_cancelaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_ncd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);
