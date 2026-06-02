-- El cliente General ya se inserta en V12, esta migración es por si acaso
INSERT IGNORE INTO clientes (id, nombre, rfc, calle, numero, fraccionamiento, c_p, municipio, estado, activo) VALUES
('c1b2c3d4-e5f6-7890-abcd-ef1234567813', 'General', 'GENERAL000000', 'N/A', 0, 'N/A', '00000', 'Aguascalientes', 'Aguascalientes', TRUE);
