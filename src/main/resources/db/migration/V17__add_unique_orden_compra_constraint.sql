ALTER TABLE orden_compras
ADD CONSTRAINT uk_orden_compra_cliente_partida_semana
UNIQUE (cliente_id, partida, fecha_inicio_semana);
