package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.productomapper.ProductoMapper;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import com.laveronica.siscontrol.domain.productos.dto.DatosActualizarProducto;
import com.laveronica.siscontrol.domain.categoria.Categoria;
import com.laveronica.siscontrol.domain.productos.dto.DatosDetalleProducto;
import com.laveronica.siscontrol.domain.productos.dto.DatosListarProductos;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.utils.helpers.CategoriaValidacionesHelper;
import com.laveronica.siscontrol.domain.productos.validaciones.ValidadorDeProductos;
import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.enums.UnidadMedida;
import com.laveronica.siscontrol.utils.helpers.PartidaValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ProductoValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.UnidadMedidaValidacionesHelper;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {
    //TODO @Qualifier estudiar
    private final ProductoMapper productoMapper;

    private final PartidaValidacionesHelper partidaValidacionesHelper;

    private final CategoriaValidacionesHelper categoriaValidacionesHelper;

    private final UnidadMedidaValidacionesHelper unidadMedidaValidacionesHelper;

    private final List<ValidadorDeProductos> validadores;

    private final ProductosRepository productosRepository;

    private final ProductoValidacionesHelper productoValidacionesHelper;

    @Transactional
    public DatosDetalleProducto registrarProducto(DatosRegistroProducto datos) {

        var nombre = productoValidacionesHelper.validarNombreNoExista(datos);
        validadores.forEach(v -> v.validar(datos));
        var partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
        var categoria = categoriaValidacionesHelper.validarCategoriaActiva(datos.categoriaId());
        var nuevoProducto = productoMapper.toEntity(datos, partida, categoria);
        nuevoProducto.setNombre(nombre);
        if (datos.codigo() != null && !datos.codigo().isBlank()) {
            nuevoProducto.setCodigo(datos.codigo().toUpperCase().trim());
        } else {
            nuevoProducto.setCodigo("PROD-" + System.currentTimeMillis());
        }
        nuevoProducto.setClaveProductoServicio(datos.claveProductoServicio());
        nuevoProducto.setClaveUnidadMedida(datos.claveUnidadMedida());
        nuevoProducto.setImpuesto(datos.impuesto());
        nuevoProducto.setDescuentos(datos.descuentos());
        nuevoProducto.setIeps1(datos.ieps1());
        nuevoProducto.setIeps2(datos.ieps2());
        nuevoProducto.setRetencion1Tipo(datos.retencion1Tipo());
        nuevoProducto.setRetencion1(datos.retencion1());
        nuevoProducto.setRetencion2Tipo(datos.retencion2Tipo());
        nuevoProducto.setRetencion2(datos.retencion2());
        nuevoProducto.setRetencion3Tipo(datos.retencion3Tipo());
        nuevoProducto.setRetencion3(datos.retencion3());
        nuevoProducto.setIdExterno(datos.idExterno());
        var producto = productosRepository.save(nuevoProducto);
        return productoMapper.toDetalleDto(producto);
    }


    public Page<DatosListarProductos> listaProductos(Pageable paguinas) {
        var page = productosRepository.findAllByActivoTrue(paguinas).map(DatosListarProductos::new);
        return page;
    }

    public Page<DatosListarProductos> listaProductosPartida(Pageable paguinas, String partida) {
        Partida partidaEnum = partidaValidacionesHelper.validaPartidaExistaString(partida);
        return productosRepository.findAllByPartidaAndActivoTrue(partidaEnum, paguinas).map(DatosListarProductos::new);
    }

    public Page<DatosListarProductos> listaProdictosCategoriaId(String id, Pageable paguinas) {
        Categoria categoria = categoriaValidacionesHelper.validarCategoriaActiva(id);
        return productosRepository.findAllByCategoriaAndActivoTrue(categoria, paguinas).map(DatosListarProductos::new);
    }


    public DatosDetalleProducto buscarProductoId(String id) {

        Producto productoEncontrado = productosRepository.findByIdAndActivoTrue(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("El Id introducido no corresponde a ningun producto")
                );
        return new DatosDetalleProducto(productoEncontrado);

    }


    public DatosDetalleProducto buscarProductoNombre(String nombre) {
        Producto productoEncontrado = productosRepository.findByNombreAndActivoTrue(nombre)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró ningún producto con el nombre '" + nombre + "'.")
                );
        return new DatosDetalleProducto(productoEncontrado);
    }


    public Page buscarProductosPorPalabra(String palabraBuscar, Pageable paguinas) {
        var palabra = palabraBuscar.toLowerCase().trim();
        return productosRepository
                .findAllByNombreContainingAndActivoTrue(palabraBuscar, paguinas)
                .map(DatosDetalleProducto::new);
    }

    @Transactional
    public DatosDetalleProducto actualizarProductoId(String id, DatosActualizarProducto datos) {
        Producto productoActualizado = productosRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró ningún producto con el ID '" + id + "'.")
                );

        if (datos.nombre() != null) {
            String nombreNormalizado = datos.nombre().toLowerCase().trim();
            productoActualizado.setNombre(nombreNormalizado);
        }
        if (datos.partida() != null) {
            Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
            productoActualizado.setPartida(partida);
        }
        if (datos.categoriaId() != null) {
            Categoria categoria = categoriaValidacionesHelper.validarCategoriaActiva(datos.categoriaId());
            productoActualizado.setCategoria(categoria);
        }
        if (datos.unidadMedida() != null) {
            UnidadMedida unidadMedida = unidadMedidaValidacionesHelper.validar(datos.unidadMedida());
            productoActualizado.setUnidadMedida(unidadMedida);
        }
        if (datos.precioCompra() != null) {
            productoActualizado.setPrecioCompra(datos.precioCompra());
        }
        if (datos.precioVenta() != null) {
            productoActualizado.setPrecioVenta(datos.precioVenta());
        }
        if (datos.codigo() != null) {
            productoActualizado.setCodigo(datos.codigo().toUpperCase().trim());
        }
        if (datos.claveProductoServicio() != null) {
            productoActualizado.setClaveProductoServicio(datos.claveProductoServicio());
        }
        if (datos.claveUnidadMedida() != null) {
            productoActualizado.setClaveUnidadMedida(datos.claveUnidadMedida());
        }
        if (datos.impuesto() != null) {
            productoActualizado.setImpuesto(datos.impuesto());
        }
        if (datos.descuentos() != null) {
            productoActualizado.setDescuentos(datos.descuentos());
        }
        if (datos.ieps1() != null) {
            productoActualizado.setIeps1(datos.ieps1());
        }
        if (datos.ieps2() != null) {
            productoActualizado.setIeps2(datos.ieps2());
        }
        if (datos.retencion1Tipo() != null) {
            productoActualizado.setRetencion1Tipo(datos.retencion1Tipo());
        }
        if (datos.retencion1() != null) {
            productoActualizado.setRetencion1(datos.retencion1());
        }
        if (datos.retencion2Tipo() != null) {
            productoActualizado.setRetencion2Tipo(datos.retencion2Tipo());
        }
        if (datos.retencion2() != null) {
            productoActualizado.setRetencion2(datos.retencion2());
        }
        if (datos.retencion3Tipo() != null) {
            productoActualizado.setRetencion3Tipo(datos.retencion3Tipo());
        }
        if (datos.retencion3() != null) {
            productoActualizado.setRetencion3(datos.retencion3());
        }
        if (datos.idExterno() != null) {
            productoActualizado.setIdExterno(datos.idExterno());
        }
        return new DatosDetalleProducto(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(String id) {
        Producto eliminar = productosRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No se encontró ningún producto con el ID '" + id + "'.")
                );
        eliminar.setActivo(false);
    }
}
