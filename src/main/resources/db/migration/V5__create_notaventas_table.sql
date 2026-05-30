CREATE TABLE nota_ventas (

    id VARCHAR(36) PRIMARY KEY,
    folio INT UNIQUE,
    fecha DATETIME NOT NULL,
    cliente_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36),
    partida VARCHAR(100) NOT NULL,
    total_general DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL

);
