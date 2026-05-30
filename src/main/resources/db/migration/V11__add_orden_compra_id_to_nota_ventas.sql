ALTER TABLE nota_ventas
    ADD COLUMN orden_compra_id BIGINT,
    ADD CONSTRAINT fk_notaventa_orden_compra
        FOREIGN KEY (orden_compra_id) REFERENCES orden_compras(id);
