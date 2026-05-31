CREATE TABLE clientes (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rfc VARCHAR(50) UNIQUE,
    calle VARCHAR(255),
    numero INT,
    fraccionamiento VARCHAR(255),
    c_p VARCHAR(50) NOT NULL,
    municipio VARCHAR(255),
    estado VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

