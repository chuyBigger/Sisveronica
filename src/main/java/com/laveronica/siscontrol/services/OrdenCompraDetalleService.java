package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.ordencompra.OrdenCompra;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.OrdenCompraDetalleMapper;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosActualizarOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosRegistroOrdenCompraDetalle;
import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenCompraDetalleService {

    @Autowired
    private ProductoValidacionesHelper productoValidacionesHelper;

    @Autowired
    private OrdenCompraDetalleMapper ordenCompraDetalleMapper;

    public List<OrdenCompraDetalle> registrarListaDetallesOrdenCompra(List<DatosRegistroOrdenCompraDetalle> datos, OrdenCompra ordenCompraNueva) {

        List<OrdenCompraDetalle> detalles = datos.stream()
                .map(
                        dn -> {
                            Producto producto = productoValidacionesHelper.encontrarProductoId(dn.producto());
                            return new OrdenCompraDetalle(dn, producto, ordenCompraNueva);
                        }
                ).collect(Collectors.toList());
        return detalles;
    }

    @Transactional
    public void actualizarListaDetallesOrdenCompra(List<DatosActualizarOrdenCompraDetalle> datos, OrdenCompra ordenCompra) {
        for (DatosActualizarOrdenCompraDetalle dto : datos){
            OrdenCompraDetalle detalleExistente = ordenCompra.getDetalles().stream()
                    .filter(ocd -> ocd.getProducto().getId()
                            .equals(dto.productoId()))
                    .findFirst()
                    .orElse(null);
            if (detalleExistente != null){
                ordenCompraDetalleMapper.actulizaEntidadesDto(dto, detalleExistente);
            }else {
                Producto producto = productoValidacionesHelper.encontrarProductoId(dto.productoId());
                OrdenCompraDetalle detalleNuevo = new OrdenCompraDetalle();
                detalleNuevo.setProducto(producto);
                ordenCompraDetalleMapper.actulizaEntidadesDto(dto, detalleNuevo);
                ordenCompra.getDetalles().add(detalleNuevo);
            }

        }
    }


}
