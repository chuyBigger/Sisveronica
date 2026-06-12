package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.reporte.dto.*;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.repositories.OrdenCompraRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteProduccionService {

    private final OrdenCompraRespository ordenCompraRepository;

    private static final String[] DIAS_ORDEN = {"martes", "miercoles", "jueves", "viernes", "sabado", "domingo", "lunes"};
    private static final Map<String, String> DIAS_LABEL = Map.of(
        "martes", "Martes",
        "miercoles", "Miércoles",
        "jueves", "Jueves",
        "viernes", "Viernes",
        "sabado", "Sábado",
        "domingo", "Domingo",
        "lunes", "Lunes"
    );

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("d 'de' MMMM", new Locale("es", "MX"));

    public DatosReporteProduccionCarne generarReporte(LocalDate fechaInicioSemana) {
        List<OrdenCompra> ordenes = ordenCompraRepository
                .findByActivoTrueAndFechaInicioSemanaAndPartida(fechaInicioSemana, Partida.CARNES);

        String semanaInicio = fechaInicioSemana.plusDays(1).format(FMT); // martes
        String semanaFin = fechaInicioSemana.plusDays(7).format(FMT);   // lunes

        // Group: client -> (day -> (product -> quantity))
        Map<String, Map<String, Map<String, DatosProductoReporte>>> clientesMap = new LinkedHashMap<>();
        // Track per-client per-day total and all dates
        Map<String, Map<String, Double>> clientesDiaTotal = new LinkedHashMap<>();
        Map<String, Map<String, LocalDate>> clientesDiaFecha = new LinkedHashMap<>();

        for (OrdenCompra oc : ordenes) {
            String clienteNombre = oc.getCliente().getNombre();
            clientesMap.putIfAbsent(clienteNombre, new LinkedHashMap<>());
            clientesDiaTotal.putIfAbsent(clienteNombre, new LinkedHashMap<>());
            clientesDiaFecha.putIfAbsent(clienteNombre, new LinkedHashMap<>());

            for (OrdenCompraDetalle det : oc.getDetalles()) {
                if (!det.isActivo()) continue;
                Producto p = det.getProducto();
                String um = p.getUnidadMedida().name();

                for (int i = 0; i < DIAS_ORDEN.length; i++) {
                    String dia = DIAS_ORDEN[i];
                    double val = getDiaValue(det, dia);
                    if (val == 0) continue;

                    LocalDate fechaReal = fechaInicioSemana.plusDays(i + 1);
                    clientesDiaFecha.get(clienteNombre).putIfAbsent(dia, fechaReal);
                    clientesDiaTotal.get(clienteNombre).merge(dia, val, Double::sum);
                    clientesMap.get(clienteNombre).putIfAbsent(dia, new LinkedHashMap<>());
                    clientesMap.get(clienteNombre).get(dia).merge(
                        p.getNombre(),
                        new DatosProductoReporte(p.getNombre(), val, um),
                        (a, b) -> new DatosProductoReporte(a.productoNombre(), a.cantidad() + b.cantidad(), a.unidadMedida())
                    );
                }
            }
        }

        List<DatosClienteReporte> clientes = new ArrayList<>();
        for (Map.Entry<String, Map<String, Map<String, DatosProductoReporte>>> entry : clientesMap.entrySet()) {
            String clienteNombre = entry.getKey();
            Map<String, Map<String, DatosProductoReporte>> diasMap = entry.getValue();
            double totalGeneral = 0;

            List<DatosDiaReporte> diasList = new ArrayList<>();
            for (String dia : DIAS_ORDEN) {
                Map<String, DatosProductoReporte> productosMap = diasMap.get(dia);
                if (productosMap == null || productosMap.isEmpty()) continue;

                String fechaStr = clientesDiaFecha.get(clienteNombre).get(dia).format(FMT);
                double totalDia = clientesDiaTotal.get(clienteNombre).get(dia);
                totalGeneral += totalDia;

                List<DatosProductoReporte> productos = new ArrayList<>(productosMap.values());
                diasList.add(new DatosDiaReporte(dia, fechaStr, productos, totalDia));
            }

            clientes.add(new DatosClienteReporte(clienteNombre, totalGeneral, diasList));
        }

        return new DatosReporteProduccionCarne(semanaInicio, semanaFin, clientes);
    }

    private double getDiaValue(OrdenCompraDetalle det, String dia) {
        return switch (dia) {
            case "lunes" -> zero(det.getLunes());
            case "martes" -> zero(det.getMartes());
            case "miercoles" -> zero(det.getMiercoles());
            case "jueves" -> zero(det.getJueves());
            case "viernes" -> zero(det.getViernes());
            case "sabado" -> zero(det.getSabado());
            case "domingo" -> zero(det.getDomingo());
            default -> 0;
        };
    }

    private double zero(Double v) {
        return v == null ? 0.0 : v;
    }
}
