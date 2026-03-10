package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.productos.Producto;
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
import com.laveronica.siscontrol.utils.helpers.UnidadMedidaValidacionesHelper;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    //TODO @Qualifier estudiar
    private PartidaValidacionesHelper partidaValidacionesHelper;

    @Autowired
    private CategoriaValidacionesHelper categoriaValidacionesHelper;

    @Autowired UnidadMedidaValidacionesHelper unidadMedidaValidacionesHelper;

    @Autowired

    private List<ValidadorDeProductos> validadores;

    @Autowired
    private ProductosRepository productosRepository;

    @Transactional
    public Producto registrarProducto(DatosRegistroProducto datos){

        var nombre = datos.nombre().toLowerCase().trim();

        var datosNormalizados = new DatosRegistroProducto(
                datos.nombre().trim().toLowerCase(),
                datos.partida(),
                datos.categoriaId(),
                datos.unidadMedida(),
                datos.precioCompra(),
                datos.precioVenta()
        );

        validadores.forEach(v -> v.validar(datosNormalizados));

        var partida = partidaValidacionesHelper.validaPartidaExistaString(datosNormalizados.partida());
        var categoria = categoriaValidacionesHelper.validarCategoriaActiva(datosNormalizados.categoriaId());
        var nuevoProducto = new Producto(datosNormalizados, partida, categoria);
        productosRepository.save(nuevoProducto);
        return nuevoProducto;
    }


    public Page<DatosListarProductos> listaProductos(Pageable paguinas) {
        var page = productosRepository.findAllByActivoTrue(paguinas).map(DatosListarProductos::new);
        return page;
    }


    public Page<DatosListarProductos> listaProductosPartida(Pageable paguinas, String partida) {

        Partida partidaEnum = partidaValidacionesHelper.validaPartidaExistaString(partida);
        var page = productosRepository.findAllByPartidaAndActivoTrue(partidaEnum, paguinas).map(DatosListarProductos::new);
        if (page.isEmpty()){
            throw new ResourceNotFoundException("No se encontraron productos activos para la partida.");
        }
        return page;
    }


    public Page<DatosListarProductos> listaProdictosCategoriaId(Long id, Pageable paguinas) {

        Categoria categoria = categoriaValidacionesHelper.validarCategoriaActiva(id);
        var page = productosRepository.findAllByCategoriaAndActivoTrue(categoria, paguinas).map(DatosListarProductos::new);
        if (page.isEmpty()){
            throw new ResourceNotFoundException("No se encontraron productos activos para la Categoria.");
        }
        return page;
    }


    public DatosDetalleProducto buscarProductoId(Long id) {

        Producto productoEncontrado = productosRepository.findByIdAndActivoTrue(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("El Id introducido no corresponde a ningun producto")
                );
        return new DatosDetalleProducto(productoEncontrado);

    }


    public DatosDetalleProducto buscarProductoNombre(String nombre) {
        Producto productoEncontrado = productosRepository.findByNombreAndActivoTrue(nombre)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No existe producto en el '"+nombre+"' registrado")
                );
        return new DatosDetalleProducto(productoEncontrado);
    }


    public Page buscarProductosPorPalabra(String palabraBuscar, Pageable paguinas) {
        var palabra = palabraBuscar.toLowerCase().trim();
        var productosEncontrados = productosRepository
                .findAllByNombreContainingAndActivoTrue(palabraBuscar, paguinas)
                .map(DatosDetalleProducto::new);
        if (productosEncontrados.isEmpty()){
            throw new ResourceNotFoundException("No existe coincidecias p productos que contengan '"+palabraBuscar+"' en el registro.");
        }
        return productosEncontrados;

    }

    @Transactional
    public DatosDetalleProducto actualizarProductoId(Long id, DatosActualizarProducto datos) {
        Producto productoActualizado = productosRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No existe producto con el id: "+id+" o esta mal escrito")
                );

        if (datos.nombre() != null){
            String nombreNormalizado = datos.nombre().toLowerCase().trim();
            productoActualizado.setNombre(nombreNormalizado);
        }
        if (datos.partida() != null){
            Partida partida = partidaValidacionesHelper.validaPartidaExistaString(datos.partida());
            productoActualizado.setPartida(partida);
        }
        if (datos.categoriaId() != null) {
            Categoria categoria = categoriaValidacionesHelper.validarCategoriaActiva(datos.categoriaId());
            productoActualizado.setCategoria(categoria);
        }
        if (datos.unidadMedida() != null){
            UnidadMedida unidadMedida = unidadMedidaValidacionesHelper.validar(datos.unidadMedida());
            productoActualizado.setUnidadMedida(unidadMedida);
        }
        if (datos.precioCompra() != null){
            productoActualizado.setPrecioCompra(datos.precioCompra());
        }
        if (datos.precioVenta() != null){
            productoActualizado.setPrecioVenta(datos.precioVenta());
        }
        return new DatosDetalleProducto(productoActualizado);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        Producto eliminar = productosRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No hay un producto con el el id "+id+"registrado")
                );
        eliminar.setActivo(false);
    }
}
