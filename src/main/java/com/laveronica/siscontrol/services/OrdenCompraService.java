package com.laveronica.siscontrol.services;


import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosActulizarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosListarOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosDetalleOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompra.dto.DatosRegistroOrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.enums.Partida;
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
import java.util.List;

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

    public DatosDetalleOrdenCompra registrarOrdenCompra(@Valid DatosRegistroOrdenCompra datos) {
        Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        ordenCompraValidacionesHelper.validaOrdenCompraExiste(datos.fechaInicioSemana(), partida);
        Cliente cliente = clienteValidacionesHelper.validaClienteExistaId(datos.cliente_id());
        Contrato contrato = contratoValidacionesHelper.validaContratoExisteId(datos.cliente_id());
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

    public DatosDetalleOrdenCompra buscarOrdenCompraId(String id) {
        return new DatosDetalleOrdenCompra(ordenCompraValidacionesHelper.buscarOrdenCompraId(id));
    }

    @Transactional
    public DatosDetalleOrdenCompra actulizarOrdenCompraId(String id, @Valid DatosActulizarOrdenCompra datos) {

        OrdenCompra ordenCompra = ordenCompraValidacionesHelper.buscarOrdenCompraId(id);
        if (datos.clienteId() != null) {
            ordenCompra.setCliente(clienteValidacionesHelper.validaClienteExistaId(datos.clienteId()));
        }
        if (datos.contrato_id() != null) {
            ordenCompra.setContrato(contratoValidacionesHelper.validaContratoExisteId(datos.contrato_id()));
        }
        if (datos.partida() != null) {
            ordenCompra.setPartida(partidaValidacionesHelper.validaPartidaExistaString(datos.partida()));
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

}
