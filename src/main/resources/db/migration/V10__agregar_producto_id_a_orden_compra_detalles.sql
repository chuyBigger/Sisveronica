ALTER TABLE orden_compra_detalles
ADD COLUMN producto_id VARCHAR(36) NOT NULL AFTER orden_compra_id;
