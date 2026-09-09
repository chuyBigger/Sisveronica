ALTER TABLE productos
    ADD COLUMN clave_producto_servicio VARCHAR(50),
    ADD COLUMN clave_unidad_medida VARCHAR(50),
    ADD COLUMN impuesto DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN descuentos DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN ieps1 DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN ieps2 DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN retencion1_tipo VARCHAR(50),
    ADD COLUMN retencion1 DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN retencion2_tipo VARCHAR(50),
    ADD COLUMN retencion2 DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN retencion3_tipo VARCHAR(50),
    ADD COLUMN retencion3 DECIMAL(10,2) DEFAULT 0,
    ADD COLUMN id_externo BIGINT;
