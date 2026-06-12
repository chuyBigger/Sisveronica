package com.laveronica.siscontrol.domain.ordencompradetalle;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "orden_compra_detalles")
@Entity(name = "orden_compra_detalle")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdenCompraDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private Double lunes;

    private Double martes;
    private Double miercoles;
    private Double jueves;
    private Double viernes;
    private Double sabado;
    private Double domingo;

    @Column(nullable = false)
    private boolean activo;

    public OrdenCompraDetalle(DatosRegistroOrdenCompraDetalle datos, Producto producto, OrdenCompra ordenCompra) {
        this.id = null;
        this.ordenCompra = ordenCompra;
        this.fecha = datos.fecha();
        this.producto = producto;
        this.lunes = datos.lunes();
        this.martes = datos.martes();
        this.miercoles = datos.miercoles();
        this.jueves = datos.jueves();
        this.viernes = datos.viernes();
        this.sabado = datos.sabado();
        this.domingo = datos.domingo();
        this.activo = true;
    }

}
