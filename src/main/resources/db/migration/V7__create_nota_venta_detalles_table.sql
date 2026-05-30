CREATE TABLE nota_venta_detalles (

    id VARCHAR(36) PRIMARY KEY,
    cantidad INTEGER NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    sub_total DECIMAL(10,2) NOT NULL,
    notaventa_id VARCHAR(36) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE

);
