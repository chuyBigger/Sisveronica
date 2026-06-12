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

INSERT INTO clientes (id, nombre, rfc, calle, numero, fraccionamiento, c_p, municipio, estado, activo) VALUES
('c1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Restaurante El Sazón',    'RSA120101ABC', 'Hidalgo',             123, 'Centro',      '20000', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Comedor La Familia',      'CLF850201DEF', 'Zaragoza',             45, 'San Marcos',  '20100', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Hotel Real Inn',          'HRI900301GHI', 'Blvd. Universitario', 500, 'Zona Centro', '20200', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567804', 'Taquería El Güero',       'TEG780401JKL', 'Av. Independencia',    78, 'Colonia Obraje','20300','Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567805', 'Cocina Doña Mary',        'CDM650501MNO', 'Calle 5 de Mayo',     234, 'San Pedro',   '20400', 'Aguascalientes', 'Aguascalientes', TRUE),
('c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'General',                  'GENERAL000000','N/A',                   0, 'N/A',         '00000', 'Aguascalientes', 'Aguascalientes', TRUE);
