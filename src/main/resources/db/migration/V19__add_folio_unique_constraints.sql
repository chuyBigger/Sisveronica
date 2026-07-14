ALTER TABLE extras ADD CONSTRAINT uq_extras_folio UNIQUE (folio);
ALTER TABLE facturas ADD CONSTRAINT uq_facturas_folio UNIQUE (folio);
