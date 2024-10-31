package um.prog2.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static um.prog2.domain.OpcionTestSamples.*;
import static um.prog2.domain.PersonalizacionTestSamples.*;

import org.junit.jupiter.api.Test;
import um.prog2.web.rest.TestUtil;

class OpcionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opcion.class);
        Opcion opcion1 = getOpcionSample1();
        Opcion opcion2 = new Opcion();
        assertThat(opcion1).isNotEqualTo(opcion2);

        opcion2.setId(opcion1.getId());
        assertThat(opcion1).isEqualTo(opcion2);

        opcion2 = getOpcionSample2();
        assertThat(opcion1).isNotEqualTo(opcion2);
    }

    @Test
    void personalizacionTest() {
        Opcion opcion = getOpcionRandomSampleGenerator();
        Personalizacion personalizacionBack = getPersonalizacionRandomSampleGenerator();

        opcion.setPersonalizacion(personalizacionBack);
        assertThat(opcion.getPersonalizacion()).isEqualTo(personalizacionBack);

        opcion.personalizacion(null);
        assertThat(opcion.getPersonalizacion()).isNull();
    }
}
