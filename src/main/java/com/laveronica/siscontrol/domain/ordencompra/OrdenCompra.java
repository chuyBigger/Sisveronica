package com.laveronica.siscontrol.domain.ordencompra;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orden_Compras")
@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")

public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partida partida;

    @Column(name = "fecha_inicio_semana", nullable = false)
    private LocalDate fechaInicioSemana;

    @Column(name = "fecha_fin_semana", nullable = false)
    private LocalDate fechaFinSemana;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenCompraDetalle> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "ordenCompra")
    private List<NotaVenta> ListaNotaVentas = new ArrayList<>();

    @Column(nullable = false)
    private Boolean activo;

    public void agregarDetalles(OrdenCompraDetalle detalle) {
        detalles.add(detalle);
        detalle.setOrdenCompra(this);
    }

    public OrdenCompra(DatosRegistroOrdenCompra datos, Cliente cliente, Contrato contrato, Partida partida, LocalDate fechaFinSemana){
        this.id = null;
        this.cliente = cliente;
        this.contrato = contrato;
        this.partida = partida;
        this.fechaInicioSemana = datos.fechaInicioSemana();
        this.fechaFinSemana = fechaFinSemana;
        this.detalles = new ArrayList<>();
        this.activo = true;
    }

}
