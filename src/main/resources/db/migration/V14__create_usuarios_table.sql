CREATE TABLE usuarios (
    id VARCHAR(36) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id)
);

INSERT INTO usuarios (id, username, password, role, activo) VALUES
('u1a2b3c4-d5e6-f789-abcd-ef1234567801', 'admin', '$2a$10$NVJdUGgq4uAZHNf9mcwAgey3qskS83J/unbSt/sKUWWXb7KuS9x.a', 'ADMIN', true),
('u1a2b3c4-d5e6-f789-abcd-ef1234567802', 'usuario', '$2a$10$.zOhm4EIW6J/8CwU.Mz3SOR/BhtniGDAceiusljicm73XEko1Yusy', 'USER', true),
('u1a2b3c4-d5e6-f789-abcd-ef1234567803', 'visita', '$2a$10$wyWDj2So5fu19fQ1pzRyYu5VbQuymBMtawX4hOreLqQ3MCnn6zLSy', 'VIEWER', true);
