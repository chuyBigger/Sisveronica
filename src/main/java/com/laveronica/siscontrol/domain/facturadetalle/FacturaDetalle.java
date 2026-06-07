package com.laveronica.siscontrol.domain.facturadetalle;

import com.laveronica.siscontrol.domain.factura.Factura;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "factura_detalles")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class FacturaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @Column(name = "producto_nombre", nullable = false)
    private String productoNombre;

    @Column(name = "cantidad_total", nullable = false)
    private Double cantidadTotal;

    @Column(name = "precio_venta", nullable = false)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private BigDecimal subtotal;
}
