CREATE TABLE productos (
    id VARCHAR(36) PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    partida VARCHAR(50) NOT NULL,
    categoria_id VARCHAR(36),
    unidad_medida VARCHAR(50) NOT NULL,
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

