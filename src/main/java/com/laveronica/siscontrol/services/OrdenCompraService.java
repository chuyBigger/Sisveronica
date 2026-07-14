package com.laveronica.siscontrol.services;


import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosGenerarNotaDesdeOrden;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.repositories.FacturaRepository;
import com.laveronica.siscontrol.repositories.NotaCancelacionRepository;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ContratoValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenCompraService {

    private final OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;
    private final ClienteValidacionesHelper clienteValidacionesHelper;
    private final ContratoValidacionesHelper contratoValidacionesHelper;
    private final OrdenCompraDetalleService ordenCompraDetalleService;
    private final OrdenCompraRespository ordenCompraRespository;
    private final PartidaValidacionesHelper partidaValidacionesHelper;
    private final NotaVentaRepository notaVentaRepository;
    private final NotaVentaService notaVentaService;
    private final FacturaRepository facturaRepository;
    private final NotaCancelacionRepository notaCancelacionRepository;

    public DatosDetalleOrdenCompra registrarOrdenCompra(@Valid DatosRegistroOrdenCompra datos) {
        Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        ordenCompraValidacionesHelper.validaOrdenCompraExiste(datos.cliente_id(), datos.fechaInicioSemana(), partida);
        Cliente cliente = clienteValidacionesHelper.validaClienteExistaId(datos.cliente_id());
        Contrato contrato = contratoValidacionesHelper.validaContratoExisteId(datos.contrato_id());
        LocalDate fechaTerminoContrato = datos.fechaInicioSemana().plusDays(6);
        OrdenCompra ordenCompraNueva = new OrdenCompra(datos, cliente, contrato, partida, fechaTerminoContrato);
        List<OrdenCompraDetalle> detalles = ordenCompraDetalleService.registrarListaDetallesOrdenCompra(datos.detalles(), ordenCompraNueva);
        ordenCompraNueva.setDetalles(detalles);
        ordenCompraRespository.save(ordenCompraNueva);
        return new DatosDetalleOrdenCompra(ordenCompraNueva);
    }

    public Page<DatosListarOrdenCompra> listarOrdenesCompra(Pageable paginacion) {
        Page<OrdenCompra> page = ordenCompraRespository.findByAndActivoTrue(paginacion);
        return attachFacturaStatus(page);
    }

    public Page<DatosListarOrdenCompra> listarOrdenesCompraPorFecha(LocalDate fecha, Pageable paginacion) {
        Page<OrdenCompra> page = ordenCompraRespository.findByFechaInPeriodo(fecha, paginacion);
        return attachFacturaStatus(page);
    }

    private Page<DatosListarOrdenCompra> attachFacturaStatus(Page<OrdenCompra> page) {
        Set<String> ocIds = page.stream().map(OrdenCompra::getId).collect(Collectors.toSet());
        if (ocIds.isEmpty()) {
            return page.map(oc -> new DatosListarOrdenCompra(oc, false, "PENDIENTE", 0L, 0L, 0L, 0L));
        }
        Set<String> idsConFactura = facturaRepository.findOrdenCompraIdsWithFactura(ocIds);

        Map<String, Long[]> notaCounts = new HashMap<>();
        for (Object[] row : notaVentaRepository.findNotaCountsByOrdenCompraIds(ocIds)) {
            String id = (String) row[0];
            Long total = (Long) row[1];
            Long firmadas = row[2] != null ? (Long) row[2] : 0L;
            notaCounts.put(id, new Long[]{total, firmadas});
        }

        Map<String, Long[]> cancelCounts = new HashMap<>();
        for (Object[] row : notaCancelacionRepository.findCancelacionCountsByOrdenCompraIds(ocIds)) {
            String id = (String) row[0];
            Long total = (Long) row[1];
            Long validadas = row[2] != null ? (Long) row[2] : 0L;
            cancelCounts.put(id, new Long[]{total, validadas});
        }

        return page.map(oc -> {
            String id = oc.getId();
            boolean tieneFactura = idsConFactura.contains(id);
            Long[] nc = notaCounts.getOrDefault(id, new Long[]{0L, 0L});
            Long[] cc = cancelCounts.getOrDefault(id, new Long[]{0L, 0L});
            Long totalNotas = nc[0], notasFirmadas = nc[1];
            Long totalCancel = cc[0], cancelValidadas = cc[1];

            String estado;
            if (tieneFactura) {
                estado = "PREFACTURA";
            } else if (oc.getConfirmadoPor() == null) {
                estado = "PENDIENTE";
            } else if (notasFirmadas < totalNotas) {
                estado = "FIRMAS_PENDIENTES";
            } else if (cancelValidadas < totalCancel) {
                estado = "CANCELACIONES_PENDIENTES";
            } else {
                estado = "LISTO";
            }

            return new DatosListarOrdenCompra(oc, tieneFactura, estado,
                    totalNotas, notasFirmadas, totalCancel, cancelValidadas);
        });
    }

    public DatosDetalleOrdenCompra buscarOrdenCompraId(String id) {
        return new DatosDetalleOrdenCompra(ordenCompraValidacionesHelper.buscarOrdenCompraId(id));
    }

    @Transactional
    public DatosDetalleOrdenCompra actulizarOrdenCompraId(String id, @Valid DatosActulizarOrdenCompra datos) {

        OrdenCompra ordenCompra = ordenCompraValidacionesHelper.buscarOrdenCompraId(id);
        String clienteId = datos.clienteId() != null ? datos.clienteId() : ordenCompra.getCliente().getId();
        Partida partida = datos.partida() != null ? partidaValidacionesHelper.validaPartidaExistaString(datos.partida()) : ordenCompra.getPartida();
        LocalDate fecha = datos.fechaInicioSemana() != null ? datos.fechaInicioSemana() : ordenCompra.getFechaInicioSemana();
        ordenCompraValidacionesHelper.validaOrdenCompraExisteAlActualizar(id, clienteId, fecha, partida);
        if (datos.clienteId() != null) {
            ordenCompra.setCliente(clienteValidacionesHelper.validaClienteExistaId(datos.clienteId()));
        }
        if (datos.contrato_id() != null) {
            ordenCompra.setContrato(contratoValidacionesHelper.validaContratoExisteId(datos.contrato_id()));
        }
        if (datos.partida() != null) {
            ordenCompra.setPartida(partida);
        }
        if (datos.fechaInicioSemana() != null) {
            ordenCompra.setFechaInicioSemana(datos.fechaInicioSemana());
        }
        if (datos.detalles() != null) {
            ordenCompraDetalleService.actualizarListaDetallesOrdenCompra(datos.detalles(), ordenCompra);
        }
        return new DatosDetalleOrdenCompra(ordenCompra);
    }

    @Transactional
    public void eliminarOrdenCompra(String id) {
        OrdenCompra ordenCompra = ordenCompraValidacionesHelper.buscarOrdenCompraId(id);
        ordenCompra.setActivo(false);
    }

    @Transactional
    public DatosDetalleOrdenCompra confirmarOrdenCompra(String id, String username) {
        OrdenCompra ordenCompra = ordenCompraValidacionesHelper.buscarOrdenCompraId(id);
        ordenCompra.setConfirmadoPor(username);
        ordenCompra.setFechaConfirmacion(LocalDateTime.now());
        ordenCompraRespository.save(ordenCompra);
        return new DatosDetalleOrdenCompra(ordenCompra);
    }

    public List<DatosListarNota> listarNotasPorOrden(String ordenId) {
        OrdenCompra ordenCompra = ordenCompraValidacionesHelper.buscarOrdenCompraId(ordenId);
        return notaVentaRepository.findByOrdenCompraIdAndActivoTrue(ordenId)
                .stream().map(DatosListarNota::new).collect(Collectors.toList());
    }

    @Transactional
    public List<DatosDetalleNota> generarTodasNotas(String ordenId) {
        OrdenCompra oc = ordenCompraValidacionesHelper.buscarOrdenCompraId(ordenId);
        if (facturaRepository.existsByOrdenCompraIdAndActivoTrue(oc.getId())) {
            throw new RuntimeException("La orden de compra ya tiene una factura generada. No se pueden crear nuevas notas.");
        }
        String[] dias = {"lunes", "martes", "miercoles", "jueves", "viernes", "sabado", "domingo"};
        List<DatosDetalleNota> notas = new java.util.ArrayList<>();
        for (String dia : dias) {
            try {
                notas.add(notaVentaService.generarNotaDesdeOrden(new DatosGenerarNotaDesdeOrden(ordenId, dia)));
            } catch (Exception ignored) {
            }
        }
        return notas;
    }

}
