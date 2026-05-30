package com.laveronica.siscontrol.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringValidacionesHelperTest {

    private final StringValidacionesHelper helper = new StringValidacionesHelper();

    @Test
    void normalizadorcodigosPersiteciaNormalizesCorrectly() {
        String result = helper.normalizadorcodigosPersitecia("  123-456_789  ");
        assertThat(result).isEqualTo("123-456_789");
    }

    @Test
    void normalizadorcodigosPersiteciaNullReturnsNull() {
        String result = helper.normalizadorcodigosPersitecia(null);
        assertThat(result).isNull();
    }

    @Test
    void normalizadoTextosPersisteciaLowercasesAndTrims() {
        String result = helper.normalizadoTextosPersistecia("  LECHE ENTERA  ");
        assertThat(result).isEqualTo("leche entera");
    }

    @Test
    void normalizadoTextosPersisteciaNullReturnsNull() {
        String result = helper.normalizadoTextosPersistecia(null);
        assertThat(result).isNull();
    }
}
