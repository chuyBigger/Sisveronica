package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.notacancelacion.NotaCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosListarCancelacion;
import com.laveronica.siscontrol.domain.notacancelacion.dto.DatosRegistroCancelacion;
import com.laveronica.siscontrol.domain.notacancelaciondetalle.NotaCancelacionDetalle;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotaCancelacionService {

    @Autowired
    private NotaCancelacionRepository cancelacionRepository;

    @Autowired
    private OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;

    @Autowired
    private OrdenCompraRespository ordenCompraRespository;

    @Autowired
    private ProductoValidacionesHelper productoValidacionesHelper;

    @Autowired
    private NotaVentaRepository notaVentaRepository;

    @Autowired
    private NotaVentaDetalleService notaVentaDetalleService;

    @Transactional
    public DatosListarCancelacion crearCancelacion(DatosRegistroCancelacion datos, String username) {
        OrdenCompra orden = ordenCompraValidacionesHelper.buscarOrdenCompraId(datos.ordenCompraId());
        if (!orden.getActivo()) {
            throw new ResourceNotFoundException("Orden de compra no encontrada");
        }

        NotaCancelacion nc = new NotaCancelacion();
        nc.setOrdenCompra(orden);
        nc.setDia(datos.dia());
        nc.setFechaCreacion(LocalDateTime.now());
        nc.setCreadoPor(username);
        nc.setActivo(true);
        nc.setDetalles(new ArrayList<>());

        for (var det : datos.detalles()) {
            Producto producto = productoValidacionesHelper.encontrarProductoId(det.productoId());
            NotaCancelacionDetalle ncd = new NotaCancelacionDetalle();
            ncd.setProducto(producto);
            ncd.setCantidadCancelada(det.cantidadCancelada());
            ncd.setActivo(true);
            nc.agregarDetalle(ncd);
        }

        cancelacionRepository.save(nc);
        return new DatosListarCancelacion(nc);
    }

    public List<DatosListarCancelacion> listarPorOrden(String ordenCompraId) {
        return cancelacionRepository.findByOrdenCompraIdAndActivoTrue(ordenCompraId)
                .stream().map(DatosListarCancelacion::new).collect(Collectors.toList());
    }

    @Transactional
    public DatosListarCancelacion validarCancelacion(String id, String username) {
        NotaCancelacion nc = cancelacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancelación no encontrada"));
        if (nc.getValidadoPor() != null) {
            throw new RecursoExistenteException("Esta cancelación ya fue validada");
        }
        nc.setValidadoPor(username);
        nc.setFechaValidacion(LocalDateTime.now());
        cancelacionRepository.save(nc);

        aplicarCancelacionANota(nc);
        return new DatosListarCancelacion(nc);
    }

    private void aplicarCancelacionANota(NotaCancelacion nc) {
        NotaVenta nota = notaVentaRepository
                .findByOrdenCompraIdAndDiaAndActivoTrue(
                        nc.getOrdenCompra().getId(), nc.getDia())
                .orElse(null);
        if (nota == null) return;

        for (NotaCancelacionDetalle detCancel : nc.getDetalles()) {
            String prodCancelId = detCancel.getProducto().getId();
            double cancelQty = detCancel.getCantidadCancelada();

            nota.getDetalles().removeIf(detNota -> {
                if (detNota.getProducto().getId().equals(prodCancelId)) {
                    int nuevaCant = detNota.getCantidad() - (int) Math.round(cancelQty);
                    if (nuevaCant <= 0) return true;
                    detNota.setCantidad(nuevaCant);
                    detNota.setSubTotal(detNota.getPrecioVenta().multiply(new BigDecimal(nuevaCant)));
                    return false;
                }
                return false;
            });
        }

        BigDecimal nuevoTotal = notaVentaDetalleService.calcularTotalGeneral(nota.getDetalles());
        nota.setTotalGeneral(nuevoTotal);
        notaVentaRepository.save(nota);
    }

    @Transactional
    public void eliminarCancelacion(String id) {
        NotaCancelacion nc = cancelacionRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cancelación no encontrada"));
        nc.setActivo(false);
        cancelacionRepository.save(nc);
    }

    @Transactional
    public List<DatosDetalleNota> reconstruirNotas(String ordenCompraId) {
        OrdenCompra orden = ordenCompraValidacionesHelper.buscarOrdenCompraId(ordenCompraId);
        List<NotaCancelacion> cancelaciones = cancelacionRepository
                .findByOrdenCompraIdAndActivoTrue(ordenCompraId)
                .stream().filter(nc -> nc.getValidadoPor() != null)
                .collect(Collectors.toList());

        Map<String, List<NotaCancelacion>> porDia = cancelaciones.stream()
                .collect(Collectors.groupingBy(NotaCancelacion::getDia));

        List<DatosDetalleNota> notasActualizadas = new ArrayList<>();

        for (var entry : porDia.entrySet()) {
            String dia = entry.getKey();
            List<NotaCancelacion> cancDia = entry.getValue();

            Map<String, Double> cantCancelada = new java.util.HashMap<>();
            for (NotaCancelacion nc : cancDia) {
                for (NotaCancelacionDetalle d : nc.getDetalles()) {
                    String prodId = d.getProducto().getId();
                    cantCancelada.merge(prodId, d.getCantidadCancelada(), Double::sum);
                }
            }

            NotaVenta notaExistente = notaVentaRepository
                    .findByOrdenCompraIdAndDiaAndActivoTrue(ordenCompraId, dia)
                    .orElse(null);

            if (notaExistente == null) {
                continue;
            }

            List<NotaVentaDetalle> nuevosDetalles = new ArrayList<>();
            for (var detOC : orden.getDetalles()) {
                Double cantOC = getCantidadPorDia(detOC, dia);
                if (cantOC == null || cantOC <= 0) continue;
                String prodId = detOC.getProducto().getId();
                Double cancelado = cantCancelada.getOrDefault(prodId, 0.0);
                double nuevaCant = cantOC - cancelado;
                if (nuevaCant <= 0) continue;
                NotaVentaDetalle nvd = new NotaVentaDetalle((int) Math.round(nuevaCant), detOC.getProducto(), null);
                nuevosDetalles.add(nvd);
            }

            notaExistente.getDetalles().clear();
            for (NotaVentaDetalle d : nuevosDetalles) {
                d.setNotaVenta(notaExistente);
                notaExistente.getDetalles().add(d);
            }

            BigDecimal nuevoTotal = notaVentaDetalleService.calcularTotalGeneral(nuevosDetalles);
            notaExistente.setTotalGeneral(nuevoTotal);
            notaVentaRepository.save(notaExistente);
            notasActualizadas.add(new DatosDetalleNota(notaExistente));
        }

        return notasActualizadas;
    }

    private Double getCantidadPorDia(com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle detalle, String dia) {
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
}
