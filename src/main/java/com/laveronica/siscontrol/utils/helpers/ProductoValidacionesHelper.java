package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.productos.Producto;
import com.laveronica.siscontrol.domain.productos.dto.DatosRegistroProducto;
import com.laveronica.siscontrol.infra.exceptions.ex.RuleValidationException;
import com.laveronica.siscontrol.repositories.ProductosRepository;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductoValidacionesHelper {

    @Autowired
    private ProductosRepository productosRepository;



    @Transactional
    public Producto encontrarProductoId(Long id) {
        var producto = productosRepository.findByIdAndActivoTrue(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("⚠️ Producto No encontrado ID:" + id + " no existe o es invalido.")
                );
        return producto;
    }

    @Transactional
    public Producto encontrarProductoNombre(String producto) {

        var productoEncontrado = productosRepository.findByNombreAndActivoTrue(producto)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe un producto valido con ese nombre: " + producto + ".")
                );
        return productoEncontrado;
    }

    public String validarNombreNoExista(DatosRegistroProducto datos){
        var nombre = datos.nombre().trim().toLowerCase();
        if (productosRepository.existsByNombre(nombre)){
            throw new RuleValidationException("El nombre del producto '"+datos.nombre()+"' ya se encuentra registrado");
        }
        return nombre;
    }


}
