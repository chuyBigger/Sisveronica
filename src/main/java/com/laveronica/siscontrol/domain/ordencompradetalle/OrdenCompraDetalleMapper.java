package com.laveronica.siscontrol.domain.ordencompradetalle;

import com.laveronica.siscontrol.domain.ordencompradetalle.dto.DatosActualizarOrdenCompraDetalle;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdenCompraDetalleMapper {
    void actulizaEntidadesDto(
            DatosActualizarOrdenCompraDetalle datos,
            @MappingTarget OrdenCompraDetalle entidad
            );
}
