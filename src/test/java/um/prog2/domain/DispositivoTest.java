package um.prog2.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static um.prog2.domain.DispositivoTestSamples.*;

import org.junit.jupiter.api.Test;
import um.prog2.web.rest.TestUtil;

class DispositivoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dispositivo.class);
        Dispositivo dispositivo1 = getDispositivoSample1();
        Dispositivo dispositivo2 = new Dispositivo();
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);

        dispositivo2.setId(dispositivo1.getId());
        assertThat(dispositivo1).isEqualTo(dispositivo2);

        dispositivo2 = getDispositivoSample2();
        assertThat(dispositivo1).isNotEqualTo(dispositivo2);
    }
}
