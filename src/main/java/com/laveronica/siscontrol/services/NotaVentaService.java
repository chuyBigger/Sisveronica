package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.notaventa.NotaVenta;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosActualizarNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosListarNota;
import com.laveronica.siscontrol.domain.notaventadetalle.dto.NotaVentaActualizarDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.repositories.NotaVentaRepository;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosDetalleNota;
import com.laveronica.siscontrol.domain.notaventa.dto.DatosRegistroNota;
import com.laveronica.siscontrol.domain.notaventadetalle.NotaVentaDetalle;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.utils.helpers.*;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaVentaService {

    private final NotaVentaRepository notaVentaRepository;

    private final ClienteValidacionesHelper clienteValidacionesHelper;

    private final PartidaValidacionesHelper partidaValidacionesHelper;

    public final ProductoValidacionesHelper productoValidacionesHelper;

    private final NotaVentaDetalleService notaVentaDetalleService;

    private final NotaVentaValidacionesHelper notaVentaValidacionesHelper;

    @Transactional
    public DatosDetalleNota registrarNota(DatosRegistroNota datos) {
        Cliente cliente = clienteValidacionesHelper.validaClienteExistaId(datos.clienteId());
        Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        NotaVenta notaNueva = new NotaVenta(cliente, partida);
        List<NotaVentaDetalle> detalles = notaVentaDetalleService.registrarNuevaListaNotaVentasDetalles(datos.detalles(), notaNueva);
        notaNueva.setDetalles(detalles);
        BigDecimal totalGeneral = notaVentaDetalleService.calcularTotalGeneral(detalles);
        notaNueva.setTotalGeneral(totalGeneral);
        notaVentaRepository.save(notaNueva);
        return new DatosDetalleNota(notaNueva);
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
        var partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        nota.setPartida(partida);
        List<NotaVentaDetalle> detallesExistentes = nota.getDetalles();
        List<NotaVentaActualizarDetalle> actualizarDetalles = datos.detalles();
        for (NotaVentaActualizarDetalle actualizaDetalle : actualizarDetalles) {
            Producto productonuevo = productoValidacionesHelper.encontrarProductoNombre(actualizaDetalle.producto());
            NotaVentaDetalle detalleCoicidente = detallesExistentes.stream()
                    .filter(d -> d.getProducto().getId().equals(productonuevo.getId()))
                    .findFirst()
                    .orElse(null);
            if (detalleCoicidente != null) {
                detalleCoicidente.setCantidad(actualizaDetalle.cantidad());
                var subtotal = NotaVentaDetalleService.calcularSubTotal(actualizaDetalle.cantidad(), productonuevo.getPrecioVenta());
                detalleCoicidente.setSubTotal(subtotal);
            }else {
                NotaVentaDetalle nuevaDetalle = new NotaVentaDetalle(
                        actualizaDetalle.cantidad(),
                        productonuevo,
                        nota
                );
                notaVentaDetalleService.agregarUnDetalleNuevo(nuevaDetalle);
                detallesExistentes.add(nuevaDetalle);
            }
        }
        BigDecimal nuevoTotalGeneral = notaVentaDetalleService.calcularTotalGeneral(detallesExistentes);
        nota.setTotalGeneral(nuevoTotalGeneral);
        notaVentaRepository.save(nota);
        return new DatosDetalleNota(nota);
    }

    @Transactional
    public void eliminarNota(String id) {
        NotaVenta nota = notaVentaValidacionesHelper.notaVentaExiste(id);
        nota.setActivo(false);
    }
}
