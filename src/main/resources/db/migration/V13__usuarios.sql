CREATE TABLE usuarios (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    nombre_completo VARCHAR(255) DEFAULT NULL,
    correo VARCHAR(255) DEFAULT NULL,
    numero VARCHAR(50) DEFAULT NULL,
    cargo VARCHAR(255) DEFAULT NULL
);

INSERT INTO usuarios (id, username, password, role, activo, nombre_completo, correo, numero, cargo) VALUES
('u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'admin',    '$2a$10$NVJdUGgq4uAZHNf9mcwAgey3qskS83J/unbSt/sKUWWXb7KuS9x.a', 'ADMIN',  true, 'Administrador del Sistema', 'admin@laveronica.com', '449-100-2000', 'Administrador'),
('u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'usuario',  '$2a$10$.zOhm4EIW6J/8CwU.Mz3SOR/BhtniGDAceiusljicm73XEko1Yusy', 'USER',   true, 'Usuario General',           'user@laveronica.com',  '449-100-2001', 'Usuario'),
('u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'visita',   '$2a$10$wyWDj2So5fu19fQ1pzRyYu5VbQuymBMtawX4hOreLqQ3MCnn6zLSy', 'VIEWER', true, 'Visitante',                  'visita@laveronica.com', '449-100-2002', 'Visitante');
