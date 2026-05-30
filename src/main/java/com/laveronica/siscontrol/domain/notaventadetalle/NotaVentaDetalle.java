package com.laveronica.siscontrol.domain.notaventadetalle;

import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.productos.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = ("nota_venta_detalles"))
@Entity(name = "nota_venta_detalle")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class NotaVentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notaventa_id", nullable = false)
    private NotaVenta notaVenta;

    public NotaVentaDetalle(Integer cantidad, Producto producto, NotaVenta notaVenta){
        this.id = null;
        this.cantidad = cantidad;
        this.producto = producto;
        this.precioVenta = producto.getPrecioVenta();
        this.subTotal = this.precioVenta.multiply(new BigDecimal(this.cantidad));
        this.activo = true;
        this.notaVenta = notaVenta;

    }
}
