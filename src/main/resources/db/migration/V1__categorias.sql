CREATE TABLE categorias (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    partida VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO categorias (id, nombre, partida, activo) VALUES
('b1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Lácteos Básicos',     'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Quesos Finos',        'LACTEOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Carnes de Res',       'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Aves y Pollo',        'CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Frutas Nacionales',   'FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567806', 'Verduras de Temporada','FRUTASYVERDURAS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567807', 'Abarrotes Enlatados', 'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567808', 'Cereales y Granos',   'ABARROTES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567809', 'Embutidos y Procesados','CARNES', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567810', 'Limpieza',            'VARIOS', TRUE),
('b1b2c3d4-e5f6-7890-abcd-ef1234567811', 'Carnes de Cerdo',     'CARNES', TRUE);
