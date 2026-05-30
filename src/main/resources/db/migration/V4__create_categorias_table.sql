CREATE TABLE categorias (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    partida VARCHAR(100) NOT NULL,
    activo Boolean NOT NULL DEFAULT TRUE
);

