CREATE TABLE clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    rfc VARCHAR(50) UNIQUE,
    calle VARCHAR(255),
    numero INT,
    cntrat_X varchar(255),
    fraccionamiento VARCHAR(255),
    c_p VARCHAR(50) NOT NULL,
    municipio VARCHAR(255),
    estado VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

