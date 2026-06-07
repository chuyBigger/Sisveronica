-- ============================================================================
-- V1__init.sql — Initial schema: all tables with final column set
-- ============================================================================

-- 1. CLIENTES
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

-- 2. CATEGORIAS
CREATE TABLE categorias (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    partida VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- 3. PRODUCTOS
CREATE TABLE productos (
    id VARCHAR(36) PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    partida VARCHAR(50) NOT NULL,
    categoria_id VARCHAR(36),
    unidad_medida VARCHAR(50) NOT NULL,
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- 4. CONTRATOS
CREATE TABLE contratos (
    id VARCHAR(36) PRIMARY KEY,
    contrato VARCHAR(100) NOT NULL UNIQUE,
    cliente_id VARCHAR(36) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL,
    presupuesto DECIMAL(12,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_contratos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- 5. ORDENES DE COMPRA (with confirmed columns and unique constraint)
CREATE TABLE orden_compras (
    id VARCHAR(36) PRIMARY KEY,
    cliente_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36) NOT NULL,
    partida VARCHAR(100) NOT NULL,
    fecha_inicio_semana DATE NOT NULL,
    fecha_fin_semana DATE NOT NULL,
    confirmado_por VARCHAR(100) DEFAULT NULL,
    fecha_confirmacion DATETIME DEFAULT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_orden_compra_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_orden_compra_contrato FOREIGN KEY (contrato_id) REFERENCES contratos(id),
    CONSTRAINT uk_orden_compra_cliente_partida_semana UNIQUE (cliente_id, partida, fecha_inicio_semana)
);

-- 6. ORDEN COMPRA DETALLES
CREATE TABLE orden_compra_detalles (
    id VARCHAR(36) PRIMARY KEY,
    orden_compra_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    fecha DATE NOT NULL,
    lunes DOUBLE,
    martes DOUBLE,
    miercoles DOUBLE,
    jueves DOUBLE,
    viernes DOUBLE,
    sabado DOUBLE,
    domingo DOUBLE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ocd_orden_compras FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id) ON DELETE CASCADE,
    CONSTRAINT fk_ocd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 7. NOTAS DE VENTA (with all later additions: orden_compra_id, dia, firmada, detalle)
CREATE TABLE nota_ventas (
    id VARCHAR(36) PRIMARY KEY,
    folio INT UNIQUE,
    fecha DATETIME NOT NULL,
    cliente_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36),
    orden_compra_id VARCHAR(36),
    partida VARCHAR(100) NOT NULL,
    dia VARCHAR(10) DEFAULT NULL,
    firmada BOOLEAN NOT NULL DEFAULT FALSE,
    detalle TEXT NULL,
    total_general DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_notaventa_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_notaventa_contrato FOREIGN KEY (contrato_id) REFERENCES contratos(id),
    CONSTRAINT fk_notaventa_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);

-- 8. NOTA VENTA DETALLES
CREATE TABLE nota_venta_detalles (
    id VARCHAR(36) PRIMARY KEY,
    cantidad INTEGER NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    sub_total DECIMAL(10,2) NOT NULL,
    notaventa_id VARCHAR(36) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_nvd_notaventa FOREIGN KEY (notaventa_id) REFERENCES nota_ventas(id),
    CONSTRAINT fk_nvd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 9. NOTA CANCELACIONES
CREATE TABLE nota_cancelaciones (
    id VARCHAR(36) PRIMARY KEY,
    orden_compra_id VARCHAR(36) NOT NULL,
    dia VARCHAR(10) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    creado_por VARCHAR(100),
    validado_por VARCHAR(100),
    fecha_validacion DATETIME,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_nc_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);

-- 10. NOTA CANCELACION DETALLES
CREATE TABLE nota_cancelacion_detalles (
    id VARCHAR(36) PRIMARY KEY,
    nota_cancelacion_id VARCHAR(36) NOT NULL,
    producto_id VARCHAR(36) NOT NULL,
    cantidad_cancelada DOUBLE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_ncd_nota_cancelacion FOREIGN KEY (nota_cancelacion_id) REFERENCES nota_cancelaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_ncd_producto FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- 11. FACTURAS
CREATE TABLE facturas (
    id VARCHAR(36) PRIMARY KEY,
    folio INT NOT NULL,
    orden_compra_id VARCHAR(36) NOT NULL,
    cliente VARCHAR(255) NOT NULL,
    contrato VARCHAR(255),
    partida VARCHAR(100) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    total_general DECIMAL(10,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_fact_orden_compra FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id)
);

-- 12. FACTURA DETALLES
CREATE TABLE factura_detalles (
    id VARCHAR(36) PRIMARY KEY,
    factura_id VARCHAR(36) NOT NULL,
    producto_nombre VARCHAR(255) NOT NULL,
    cantidad_total DOUBLE NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_fd_factura FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE
);

-- 13. USUARIOS
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

-- 14. USUARIO PERMISOS
CREATE TABLE usuario_permisos (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL,
    modulo VARCHAR(30) NOT NULL,
    accion VARCHAR(20) NOT NULL,
    UNIQUE KEY uk_usuario_modulo_accion (usuario_id, modulo, accion),
    CONSTRAINT fk_permiso_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
