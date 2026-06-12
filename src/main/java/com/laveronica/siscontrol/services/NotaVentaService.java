package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosActualizarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosGenerarNotaDesdeOrden;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaActualizarDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.FacturaRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.utils.helpers.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaVentaService {

    private final NotaVentaRepository notaVentaRepository;
    private final OrdenCompraRespository ordenCompraRespository;
    private final FacturaRepository facturaRepository;
    private final ClienteValidacionesHelper clienteValidacionesHelper;
    private final PartidaValidacionesHelper partidaValidacionesHelper;
    public final ProductoValidacionesHelper productoValidacionesHelper;
    private final NotaVentaDetalleService notaVentaDetalleService;
    private final NotaVentaValidacionesHelper notaVentaValidacionesHelper;

    private void verificarNotaNoFacturada(NotaVenta nota) {
        if (nota.getOrdenCompra() != null
            && facturaRepository.existsByOrdenCompraIdAndActivoTrue(nota.getOrdenCompra().getId())) {
            throw new RuntimeException("La orden de compra ya tiene una factura generada. No se puede modificar la nota.");
        }
    }

    @Transactional
    public DatosDetalleNota registrarNota(DatosRegistroNota datos) {
        Cliente cliente = clienteValidacionesHelper.validaClienteExistaId(datos.clienteId());
        Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        NotaVenta notaNueva = new NotaVenta(cliente, partida);
        Integer nextFolio = notaVentaRepository.findMaxFolio() + 1;
        notaNueva.setFolio(nextFolio);
        List<NotaVentaDetalle> detalles = notaVentaDetalleService.registrarNuevaListaNotaVentasDetalles(datos.detalles(), notaNueva);
        notaNueva.setDetalles(detalles);
        BigDecimal totalGeneral = notaVentaDetalleService.calcularTotalGeneral(detalles);
        notaNueva.setTotalGeneral(totalGeneral);
        notaVentaRepository.save(notaNueva);
        return new DatosDetalleNota(notaNueva);
    }

    @Transactional
    public DatosDetalleNota generarNotaDesdeOrden(DatosGenerarNotaDesdeOrden datos) {
        OrdenCompra orden = ordenCompraRespository.findByIdAndActivoTrue(datos.ordenCompraId())
                .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada"));
        if (facturaRepository.existsByOrdenCompraIdAndActivoTrue(orden.getId())) {
            throw new RuntimeException("La orden de compra ya tiene una factura generada. No se pueden crear nuevas notas.");
        }

        Map<String, Double> cantidadesPorDia = Map.of(
                "lunes", 0.0, "martes", 0.0, "miercoles", 0.0,
                "jueves", 0.0, "viernes", 0.0, "sabado", 0.0, "domingo", 0.0
        );

        if (!cantidadesPorDia.containsKey(datos.dia())) {
            throw new ResourceNotFoundException("Día no válido: " + datos.dia());
        }

        List<NotaVentaDetalle> detalles = new ArrayList<>();
        for (OrdenCompraDetalle detalle : orden.getDetalles()) {
            Double cantidad = getCantidadPorDia(detalle, datos.dia());
            if (cantidad != null && cantidad > 0) {
                int cantInt = (int) Math.round(cantidad);
                NotaVentaDetalle nvDetalle = new NotaVentaDetalle(cantInt, detalle.getProducto(), null);
                detalles.add(nvDetalle);
            }
        }

        if (detalles.isEmpty()) {
            throw new ResourceNotFoundException("No hay productos con cantidad para el día: " + datos.dia());
        }

        Integer nextFolio = notaVentaRepository.findMaxFolio() + 1;

        NotaVenta notaNueva = new NotaVenta();
        notaNueva.setCliente(orden.getCliente());
        notaNueva.setContrato(orden.getContrato());
        notaNueva.setOrdenCompra(orden);
        LocalDate fechaNota = calcularFechaNota(orden, datos.dia());
        notaNueva.setFecha(fechaNota.atStartOfDay());
        notaNueva.setPartida(orden.getPartida());
        notaNueva.setFolio(nextFolio);
        notaNueva.setDia(datos.dia());
        notaNueva.setDetalles(new ArrayList<>());
        notaNueva.setActivo(true);

        for (NotaVentaDetalle d : detalles) {
            d.setNotaVenta(notaNueva);
            notaNueva.getDetalles().add(d);
        }

        BigDecimal totalGeneral = notaVentaDetalleService.calcularTotalGeneral(detalles);
        notaNueva.setTotalGeneral(totalGeneral);

        notaVentaRepository.save(notaNueva);
        return new DatosDetalleNota(notaNueva);
    }

    private LocalDate calcularFechaNota(OrdenCompra orden, String dia) {
        java.util.Map<String, Integer> offsets = java.util.Map.of(
            "martes", 0, "miercoles", 1, "jueves", 2,
            "viernes", 3, "sabado", 4, "domingo", 5, "lunes", 6
        );
        Integer offset = offsets.get(dia);
        if (offset == null) return orden.getFechaInicioSemana();
        return orden.getFechaInicioSemana().plusDays(offset);
    }

    private Double getCantidadPorDia(OrdenCompraDetalle detalle, String dia) {
        return switch (dia) {
            case "lunes" -> detalle.getLunes();
            case "martes" -> detalle.getMartes();
            case "miercoles" -> detalle.getMiercoles();
            case "jueves" -> detalle.getJueves();
            case "viernes" -> detalle.getViernes();
            case "sabado" -> detalle.getSabado();
            case "domingo" -> detalle.getDomingo();
            default -> null;
        };
    }

    public Page<DatosListarNota> listarNotas(Pageable paginacion) {
        return notaVentaRepository.findAllByActivoTrue(paginacion)
                .map(DatosListarNota::new);
    }

    public DatosDetalleNota buscarNotaId(String id) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        return new DatosDetalleNota(nota);
    }

    @Transactional
    public DatosDetalleNota actualizarNota(String id, DatosActualizarNota datos) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        verificarNotaNoFacturada(nota);
        var partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        nota.setPartida(partida);

        nota.getDetalles().clear();

        List<NotaVentaDetalle> nuevosDetalles = new ArrayList<>();
        for (NotaVentaActualizarDetalle actualizaDetalle : datos.detalles()) {
            Producto productonuevo = productoValidacionesHelper.encontrarProductoId(actualizaDetalle.producto());
            NotaVentaDetalle nuevoDetalle = new NotaVentaDetalle(
                    actualizaDetalle.cantidad(),
                    productonuevo,
                    nota
            );
            nuevosDetalles.add(nuevoDetalle);
        }

        for (NotaVentaDetalle d : nuevosDetalles) {
            nota.agregarDetalles(d);
        }

        BigDecimal nuevoTotalGeneral = notaVentaDetalleService.calcularTotalGeneral(nota.getDetalles());
        nota.setTotalGeneral(nuevoTotalGeneral);
        notaVentaRepository.save(nota);
        return new DatosDetalleNota(nota);
    }

    @Transactional
    public void eliminarNota(String id) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        verificarNotaNoFacturada(nota);
        nota.setActivo(false);
    }

    @Transactional
    public DatosDetalleNota firmarNota(String id) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        nota.setFirmada(true);
        notaVentaRepository.save(nota);
        return new DatosDetalleNota(nota);
    }

    @Transactional
    public DatosDetalleNota actualizarDetalle(String id, String detalle) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        nota.setDetalle(detalle);
        notaVentaRepository.save(nota);
        return new DatosDetalleNota(nota);
    }
}
