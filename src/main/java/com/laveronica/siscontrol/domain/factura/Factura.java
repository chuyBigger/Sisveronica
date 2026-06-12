package com.laveronica.siscontrol.domain.factura;

import com.laveronica.siscontrol.domain.facturadetalle.FacturaDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "facturas")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    @Column(nullable = false)
    private String cliente;

    private String contrato;

    @Column(nullable = false)
    private String partida;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "total_general", nullable = false)
    private BigDecimal totalGeneral;

    @Column(name = "es_extras", nullable = false)
    @Builder.Default
    private Boolean esExtras = false;

    @Column(nullable = false)
    private Boolean activo;

    @Builder.Default
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaDetalle> detalles = new ArrayList<>();

    public void agregarDetalle(FacturaDetalle detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }
}
