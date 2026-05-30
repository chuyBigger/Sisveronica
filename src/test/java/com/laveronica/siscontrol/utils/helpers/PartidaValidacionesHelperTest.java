package com.laveronica.siscontrol.utils.helpers;

import com.laveronica.siscontrol.enums.Partida;
import com.laveronica.siscontrol.infra.exceptions.ex.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PartidaValidacionesHelperTest {

    private final PartidaValidacionesHelper helper = new PartidaValidacionesHelper();

    @Test
    void validaPartidaExistaStringValidPartidaReturnsEnum() {
        Partida result = helper.validaPartidaExistaString("ABARROTES");
        assertThat(result).isEqualTo(Partida.ABARROTES);
    }

    @Test
    void validaPartidaExistaStringValidPartidaLowercaseReturnsEnum() {
        Partida result = helper.validaPartidaExistaString("abarrotes");
        assertThat(result).isEqualTo(Partida.ABARROTES);
    }

    @Test
    void validaPartidaExistaStringNullThrowsResourceNotFoundException() {
        assertThatThrownBy(() -> helper.validaPartidaExistaString(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("partida es requerida");
    }

    @Test
    void validaPartidaExistaStringEmptyThrowsResourceNotFoundException() {
        assertThatThrownBy(() -> helper.validaPartidaExistaString(""))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("partida es requerida");
    }

    @Test
    void validaPartidaExistaStringInvalidThrowsResourceNotFoundException() {
        assertThatThrownBy(() -> helper.validaPartidaExistaString("INVALIDA"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no es una partida válida");
    }
}
