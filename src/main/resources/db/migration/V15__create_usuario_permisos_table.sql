CREATE TABLE usuario_permisos (
    id VARCHAR(36) NOT NULL,
    usuario_id VARCHAR(36) NOT NULL,
    modulo VARCHAR(30) NOT NULL,
    accion VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_modulo_accion (usuario_id, modulo, accion),
    CONSTRAINT fk_permiso_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Permisos por defecto para admin (todos)
INSERT INTO usuario_permisos (id, usuario_id, modulo, accion) VALUES
('p1a2b3c4-d5e6-f789-abcd-ef1234567801', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'PRODUCTOS', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567802', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'PRODUCTOS', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567803', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'PRODUCTOS', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567804', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'PRODUCTOS', 'ELIMINAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567805', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CLIENTES', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567806', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CLIENTES', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567807', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CLIENTES', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567808', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CLIENTES', 'ELIMINAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567809', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CONTRATOS', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567810', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CONTRATOS', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567811', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CONTRATOS', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567812', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'CONTRATOS', 'ELIMINAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567813', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'NOTAS_VENTA', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567814', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'NOTAS_VENTA', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567815', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'NOTAS_VENTA', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567816', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'NOTAS_VENTA', 'ELIMINAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567817', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'ORDENES_COMPRA', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567818', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'ORDENES_COMPRA', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567819', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'ORDENES_COMPRA', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567820', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'ORDENES_COMPRA', 'ELIMINAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567821', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'REPORTES', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567822', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'USUARIOS', 'CREAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567823', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'USUARIOS', 'LEER'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567824', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'USUARIOS', 'ACTUALIZAR'),
('p1a2b3c4-d5e6-f789-abcd-ef1234567825', 'u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'USUARIOS', 'ELIMINAR');

-- Permisos por defecto para usuario (solo lectura + crear ordenes)
INSERT INTO usuario_permisos (id, usuario_id, modulo, accion) VALUES
('p2a2b3c4-d5e6-f789-abcd-ef1234567801', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'PRODUCTOS', 'LEER'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567802', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'CLIENTES', 'LEER'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567803', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'CONTRATOS', 'LEER'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567804', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'NOTAS_VENTA', 'CREAR'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567805', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'NOTAS_VENTA', 'LEER'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567806', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'ORDENES_COMPRA', 'CREAR'),
('p2a2b3c4-d5e6-f789-abcd-ef1234567807', 'u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'ORDENES_COMPRA', 'LEER');

-- Permisos para viewer (solo lectura)
INSERT INTO usuario_permisos (id, usuario_id, modulo, accion) VALUES
('p3a2b3c4-d5e6-f789-abcd-ef1234567801', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'PRODUCTOS', 'LEER'),
('p3a2b3c4-d5e6-f789-abcd-ef1234567802', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'CLIENTES', 'LEER'),
('p3a2b3c4-d5e6-f789-abcd-ef1234567803', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'CONTRATOS', 'LEER'),
('p3a2b3c4-d5e6-f789-abcd-ef1234567804', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'NOTAS_VENTA', 'LEER'),
('p3a2b3c4-d5e6-f789-abcd-ef1234567805', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'ORDENES_COMPRA', 'LEER'),
('p3a2b3c4-d5e6-f789-abcd-ef1234567806', 'u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'REPORTES', 'LEER');
