package br.com.sysgipe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.sysgipe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arquivo.class);
        Arquivo arquivo1 = new Arquivo();
        arquivo1.setId(1L);
        Arquivo arquivo2 = new Arquivo();
        arquivo2.setId(arquivo1.getId());
        assertThat(arquivo1).isEqualTo(arquivo2);
        arquivo2.setId(2L);
        assertThat(arquivo1).isNotEqualTo(arquivo2);
        arquivo1.setId(null);
        assertThat(arquivo1).isNotEqualTo(arquivo2);
    }
}
