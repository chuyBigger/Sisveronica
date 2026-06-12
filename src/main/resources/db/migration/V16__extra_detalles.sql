CREATE TABLE extra_detalles (
    id VARCHAR(36) PRIMARY KEY,
    extra_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    cantidad DOUBLE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ed_extra FOREIGN KEY (extra_id) REFERENCES extras(id) ON DELETE CASCADE,
    CONSTRAINT fk_ed_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);
