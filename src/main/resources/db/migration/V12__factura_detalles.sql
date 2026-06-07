CREATE TABLE factura_detalles (
    id VARCHAR(36) PRIMARY KEY,
    factura_id VARCHAR(36) NOT NULL,
    producto_nombre VARCHAR(255) NOT NULL,
    cantidad_total DOUBLE NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_fd_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);
