package com.laveronica.siscontrol.domain.notaventa;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.enums.Partida;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "nota_ventas")
@Entity(name = "nota_venta")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class NotaVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "folio", unique = true, nullable = false )
    private Integer folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = true)
    private OrdenCompra ordenCompra;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partida partida;

    @OneToMany(mappedBy = "notaVenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaVentaDetalle> detalles = new ArrayList<>();

    @Column(name = "total_general", nullable = false)
    private BigDecimal totalGeneral;

    @Column(nullable = false)
    private Boolean activo;

    @Column(length = 10)
    private String dia;

    @Column(nullable = false)
    private Boolean firmada = false;

    @Column(columnDefinition = "TEXT")
    private String detalle;

    public void agregarDetalles(NotaVentaDetalle detalle) {
        detalles.add(detalle);
        detalle.setNotaVenta(this);
    }

    public NotaVenta(Cliente cliente, Partida partida) {
        this.id = null;
        this.folio = null;
        this.fecha = LocalDateTime.now();
        this.cliente = cliente;
        this.partida = partida;
        this.detalles = new ArrayList<>();
        this.totalGeneral = BigDecimal.ZERO;
        this.activo = true;

    }

}


