package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.domain.contratos.Contrato;
import com.laveronica.siscontrol.domain.contratos.dto.DatosDetalleContrato;
import com.laveronica.siscontrol.infra.exceptions.ex.RecursoExistenteException;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import com.laveronica.siscontrol.repositories.ContratoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ContratoValidacionesHelper {

    private final ContratoRepository contratoRepository;
    private final StringValidacionesHelper stringValidacionesHelper;

    public Contrato validaContratoExisteId(String id){
        return contratoRepository.findByIdAndActivoTrue(id).orElseThrow(
                () -> new ResourceNotFoundException("⚠️ No se encontro el contrato  o no existe...")
        );
    }

    public void validaContratoExiste(String contrato){
        var contratoLimpio = stringValidacionesHelper.normalizadorcodigosPersitecia(contrato);
        boolean existe = contratoRepository.existsByContrato(contratoLimpio);
        if (existe){
            throw new  RecursoExistenteException("⚠️ Error el registro ya existe." + contrato);
        }
    }

    public DatosDetalleContrato buscarContratoExisteId(String id) {
        return contratoRepository.findById(id)
                .map(DatosDetalleContrato::new)
                .orElseThrow(() -> new ResourceNotFoundException("⚠️ Contrato no encontrado !!"));

    }
}
