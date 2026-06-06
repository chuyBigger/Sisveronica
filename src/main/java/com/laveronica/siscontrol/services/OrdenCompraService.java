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
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ContratoValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;

    @Autowired
    private ClienteValidacionesHelper clienteValidacionesHelper;

    @Autowired
    private ContratoValidacionesHelper contratoValidacionesHelper;

    @Autowired
    private OrdenCompraDetalleService ordenCompraDetalleService;

    @Autowired
    private OrdenCompraRespository ordenCompraRespository;

    @Autowired
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Autowired
    private NotaVentaRepository notaVentaRepository;

    @Autowired
    private NotaVentaService notaVentaService;

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
        return ordenCompraRespository.findByAndActivoTrue(paginacion)
                .map(DatosListarOrdenCompra::new);
    }

    public Page<DatosListarOrdenCompra> listarOrdenesCompraPorFecha(LocalDate fecha, Pageable paginacion) {
        return ordenCompraRespository.findByFechaInPeriodo(fecha, paginacion)
                .map(DatosListarOrdenCompra::new);
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
        ordenCompraValidacionesHelper.buscarOrdenCompraId(ordenId);
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
