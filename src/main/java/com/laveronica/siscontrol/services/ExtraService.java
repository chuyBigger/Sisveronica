package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.extra.Extra;
import com.laveronica.siscontrol.domain.extra.dto.DatosListarExtra;
import com.laveronica.siscontrol.domain.extra.dto.DatosRegistroExtra;
import com.laveronica.siscontrol.domain.extradetalle.ExtraDetalle;
import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.enums.DiaSemana;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.ExtraRepository;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import com.laveronica.siscontrol.utils.helpers.OrdenCompraValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
public class ExtraService {

    private final ExtraRepository extraRepository;
    private final OrdenCompraRespository ordenCompraRepository;
    private final OrdenCompraValidacionesHelper ordenCompraValidacionesHelper;
    private final ProductoValidacionesHelper productoValidacionesHelper;

    @Transactional
    public DatosListarExtra crearExtra(DatosRegistroExtra datos, String username) {
        OrdenCompra orden = ordenCompraValidacionesHelper.buscarOrdenCompraId(datos.ordenCompraId());
        if (!orden.getActivo()) {
            throw new ResourceNotFoundException("Orden de compra no encontrada");
        }

        LocalDate fecha = calcularFecha(orden.getFechaInicioSemana(), datos.dia());
        int folio = extraRepository.findMaxFolio() + 1;

        Extra extra = Extra.builder()
                .ordenCompra(orden)
                .dia(datos.dia())
                .fecha(fecha)
                .folio(folio)
                .firmada(false)
                .fechaCreacion(LocalDateTime.now())
                .creadoPor(username)
                .activo(true)
                .detalles(new ArrayList<>())
                .build();

        for (var det : datos.detalles()) {
            Producto producto = productoValidacionesHelper.encontrarProductoId(det.productoId());
            BigDecimal precioVenta = producto.getPrecioVenta() != null ? producto.getPrecioVenta() : BigDecimal.ZERO;
            ExtraDetalle ed = ExtraDetalle.builder()
                    .producto(producto)
                    .cantidad(det.cantidad())
                    .precioVenta(precioVenta)
                    .activo(true)
                    .build();
            extra.agregarDetalle(ed);
        }

        extraRepository.save(extra);
        return new DatosListarExtra(extra);
    }

    public List<DatosListarExtra> listarPorOrden(String ordenCompraId) {
        return extraRepository.findByOrdenCompraIdAndActivoTrue(ordenCompraId)
                .stream().map(DatosListarExtra::new).collect(Collectors.toList());
    }

    @Transactional
    public DatosListarExtra firmarExtra(String id, String username) {
        Extra extra = extraRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra no encontrado"));
        if (extra.getFirmada()) {
            throw new RecursoExistenteException("Este extra ya está firmado");
        }
        extra.setFirmada(true);
        extraRepository.save(extra);
        return new DatosListarExtra(extra);
    }

    @Transactional
    public void eliminarExtra(String id) {
        Extra extra = extraRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Extra no encontrado"));
        if (extra.getFirmada()) {
            throw new RecursoExistenteException("No se puede eliminar un extra firmado");
        }
        extra.setActivo(false);
        extraRepository.save(extra);
    }

    private LocalDate calcularFecha(LocalDate fechaInicioSemana, String dia) {
        return DiaSemana.fromString(dia).calcularFecha(fechaInicioSemana);
    }
}
