package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.extra.Extra;
import com.laveronica.siscontrol.domain.factura.Factura;
import com.laveronica.siscontrol.domain.factura.dto.DatosListarFactura;
import com.laveronica.siscontrol.domain.factura.dto.DatosRegistroFactura;
import com.laveronica.siscontrol.domain.facturadetalle.FacturaDetalle;
import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.repositories.ExtraRepository;
import com.laveronica.siscontrol.repositories.FacturaRepository;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final OrdenCompraRespository ordenCompraRepository;
    private final NotaVentaRepository notaVentaRepository;
    private final NotaCancelacionRepository notaCancelacionRepository;
    private final ExtraRepository extraRepository;

    @Transactional
    public DatosListarFactura generarFactura(DatosRegistroFactura dto) {
        OrdenCompra oc = ordenCompraRepository.findById(dto.ordenCompraId())
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (oc.getConfirmadoPor() == null) {
            throw new RuntimeException("La orden de compra debe estar confirmada antes de generar la factura");
        }

        if (facturaRepository.existsByOrdenCompraIdAndActivoTrueAndEsExtrasFalse(oc.getId())) {
            throw new RuntimeException("La orden de compra ya tiene una factura generada");
        }

        List<NotaVenta> notas = notaVentaRepository.findByOrdenCompraIdAndActivoTrue(oc.getId());
        if (notas.isEmpty()) {
            throw new RuntimeException("La orden de compra no tiene notas de venta");
        }

        List<NotaVenta> notasNoFirmadas = notas.stream()
            .filter(n -> !Boolean.TRUE.equals(n.getFirmada()))
            .toList();
        if (!notasNoFirmadas.isEmpty()) {
            throw new RuntimeException("Todas las notas de venta deben estar firmadas. Pendientes: "
                + notasNoFirmadas.size());
        }

        List<NotaCancelacion> cancelaciones = notaCancelacionRepository
            .findByOrdenCompraIdAndActivoTrue(oc.getId());
        List<NotaCancelacion> cancelacionesNoValidadas = cancelaciones.stream()
            .filter(c -> c.getValidadoPor() == null)
            .toList();
        if (!cancelacionesNoValidadas.isEmpty()) {
            throw new RuntimeException("Todas las cancelaciones deben estar validadas. Pendientes: "
                + cancelacionesNoValidadas.size());
        }

        Map<String, Double> cantidadesPorProducto = new HashMap<>();
        Map<String, BigDecimal> preciosPorProducto = new HashMap<>();

        for (NotaVenta nota : notas) {
            for (var detalle : nota.getDetalles()) {
                String nombre = detalle.getProducto().getNombre();
                cantidadesPorProducto.merge(nombre, detalle.getCantidad().doubleValue(), Double::sum);
                preciosPorProducto.putIfAbsent(nombre, detalle.getPrecioVenta());
            }
        }

        for (NotaCancelacion cancelacion : cancelaciones) {
            for (var detalle : cancelacion.getDetalles()) {
                String nombre = detalle.getProducto().getNombre();
                cantidadesPorProducto.merge(nombre, -detalle.getCantidadCancelada(), Double::sum);
            }
        }

        cantidadesPorProducto.values().removeIf(v -> v <= 0);

        int folio = facturaRepository.obtenerMaxFolio() + 1;
        Factura factura = Factura.builder()
            .folio(folio)
            .ordenCompra(oc)
            .cliente(oc.getCliente().getNombre())
            .contrato(oc.getContrato() != null ? oc.getContrato().getContrato() : null)
            .partida(oc.getPartida().name())
            .fechaCreacion(LocalDateTime.now())
            .totalGeneral(BigDecimal.ZERO)
            .activo(true)
            .build();

        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (var entry : cantidadesPorProducto.entrySet()) {
            String productoNombre = entry.getKey();
            Double cantidad = entry.getValue();
            BigDecimal precio = preciosPorProducto.get(productoNombre);
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);

            FacturaDetalle detalle = FacturaDetalle.builder()
                .productoNombre(productoNombre)
                .cantidadTotal(cantidad)
                .precioVenta(precio)
                .subtotal(subtotal)
                .build();

            factura.agregarDetalle(detalle);
            totalGeneral = totalGeneral.add(subtotal);
        }

        factura.setTotalGeneral(totalGeneral);
        factura = facturaRepository.save(factura);

        return new DatosListarFactura(factura);
    }

    @Transactional
    public DatosListarFactura generarFacturaExtras(DatosRegistroFactura dto) {
        OrdenCompra oc = ordenCompraRepository.findById(dto.ordenCompraId())
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (facturaRepository.existsByOrdenCompraIdAndActivoTrueAndEsExtrasTrue(oc.getId())) {
            throw new RuntimeException("La orden de compra ya tiene una factura de extras generada");
        }

        List<Extra> extras = extraRepository.findByOrdenCompraIdAndActivoTrue(oc.getId());
        List<Extra> extrasNoFirmados = extras.stream()
            .filter(e -> !Boolean.TRUE.equals(e.getFirmada()))
            .toList();
        if (!extrasNoFirmados.isEmpty()) {
            throw new RuntimeException("Todos los extras deben estar firmados. Pendientes: "
                + extrasNoFirmados.size());
        }

        if (extras.isEmpty()) {
            throw new RuntimeException("No hay extras para facturar");
        }

        Map<String, Double> cantidadesPorProducto = new HashMap<>();
        Map<String, BigDecimal> preciosPorProducto = new HashMap<>();

        for (Extra extra : extras) {
            for (var detalle : extra.getDetalles()) {
                String nombre = detalle.getProducto().getNombre();
                cantidadesPorProducto.merge(nombre, detalle.getCantidad(), Double::sum);
            }
        }

        List<NotaVenta> notas = notaVentaRepository.findByOrdenCompraIdAndActivoTrue(oc.getId());
        for (NotaVenta nota : notas) {
            for (var detalle : nota.getDetalles()) {
                String nombre = detalle.getProducto().getNombre();
                preciosPorProducto.putIfAbsent(nombre, detalle.getPrecioVenta());
            }
        }

        int folio = facturaRepository.obtenerMaxFolio() + 1;
        Factura factura = Factura.builder()
            .folio(folio)
            .ordenCompra(oc)
            .cliente(oc.getCliente().getNombre())
            .contrato(oc.getContrato() != null ? oc.getContrato().getContrato() : null)
            .partida(oc.getPartida().name())
            .fechaCreacion(LocalDateTime.now())
            .totalGeneral(BigDecimal.ZERO)
            .esExtras(true)
            .activo(true)
            .build();

        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (var entry : cantidadesPorProducto.entrySet()) {
            String productoNombre = entry.getKey();
            Double cantidad = entry.getValue();
            BigDecimal precio = preciosPorProducto.getOrDefault(productoNombre, BigDecimal.ZERO);
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);

            FacturaDetalle detalle = FacturaDetalle.builder()
                .productoNombre(productoNombre)
                .cantidadTotal(cantidad)
                .precioVenta(precio)
                .subtotal(subtotal)
                .build();

            factura.agregarDetalle(detalle);
            totalGeneral = totalGeneral.add(subtotal);
        }

        factura.setTotalGeneral(totalGeneral);
        factura = facturaRepository.save(factura);

        return new DatosListarFactura(factura);
    }

    public List<DatosListarFactura> listarFacturas() {
        return facturaRepository.findByActivoTrueOrderByFechaCreacionDesc()
            .stream().map(DatosListarFactura::new).toList();
    }

    public Optional<DatosListarFactura> obtenerPorId(String id) {
        return facturaRepository.findById(id)
            .map(DatosListarFactura::new);
    }

    public Optional<DatosListarFactura> obtenerPorOrdenCompraId(String ordenCompraId) {
        return facturaRepository.findByOrdenCompraIdAndActivoTrueAndEsExtrasFalse(ordenCompraId)
            .map(DatosListarFactura::new);
    }

    public Optional<DatosListarFactura> obtenerFacturaExtrasPorOrdenCompraId(String ordenCompraId) {
        return facturaRepository.findByOrdenCompraIdAndActivoTrueAndEsExtrasTrue(ordenCompraId)
            .map(DatosListarFactura::new);
    }
}
