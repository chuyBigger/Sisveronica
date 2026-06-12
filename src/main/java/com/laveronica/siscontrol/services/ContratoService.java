package com.laveronica.siscontrol.services;

import com.laveronica.siscontrol.domain.clientes.Cliente;
import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.repositories.ClienteRepository;
import com.laveronica.siscontrol.domain.contratos.dto.DatosActualizarContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosDetalleContrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosRegistroContrato;
import com.laveronica.siscontrol.repositories.ContratoRepository;
import com.laveronica.siscontrol.utils.helpers.ClienteValidacionesHelper;
import com.laveronica.siscontrol.utils.helpers.ContratoValidacionesHelper;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteValidacionesHelper clienteValidacionesHelper;

    @Autowired
    private ContratoValidacionesHelper contratoValidacionesHelper;

    @Transactional
    public Contrato registrarContrato(@Valid DatosRegistroContrato datos) {
        Cliente cliente = clienteValidacionesHelper.validaClienteExistaId(datos.clienteId());
        contratoValidacionesHelper.validaContratoExiste(datos.contrato());
        return contratoRepository.save(new Contrato(datos, cliente));
    }

    public List<DatosDetalleContrato> listarContratos() {
        return contratoRepository.findAllByActivoTrue()
                .stream()
                .map(DatosDetalleContrato::new)
                .toList();
    }

    public DatosDetalleContrato buscarContratoId(@Valid String id) {
        return contratoValidacionesHelper.buscarContratoExisteId(id);
    }

    public DatosDetalleContrato actualizarContratoId(@Valid String id, DatosActualizarContrato datos) {

        Contrato contrato = contratoValidacionesHelper.validaContratoExisteId(id);

        if (datos.clienteId() != null) {
            contrato.setCliente(clienteValidacionesHelper.validaClienteExistaId(datos.clienteId()));
        }

        if (datos.fechaInicio() != null) {
            contrato.setFechaInicio(datos.fechaInicio());
        }

        if (datos.fechaTermino() != null) {
            contrato.setFechaTermino(datos.fechaTermino());
        }

        if (datos.presupuesto() != null) {
            contrato.setPresupuesto(datos.presupuesto());
        }

        Contrato contratoActulizado = contratoRepository.save(contrato);

        return new DatosDetalleContrato(contratoActulizado);
    }

    public void eliminarContrato(String id) {
        Contrato contrato = contratoValidacionesHelper.validaContratoExisteId(id);
        contrato.setActivo(false);
    }
}
