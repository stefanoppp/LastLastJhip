package um.prog2.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static um.prog2.domain.AdicionalTestSamples.*;
import static um.prog2.domain.DispositivoTestSamples.*;
import static um.prog2.domain.PersonalizacionTestSamples.*;
import static um.prog2.domain.VentaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import um.prog2.web.rest.TestUtil;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void dispositivoTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Dispositivo dispositivoBack = getDispositivoRandomSampleGenerator();

        venta.setDispositivo(dispositivoBack);
        assertThat(venta.getDispositivo()).isEqualTo(dispositivoBack);

        venta.dispositivo(null);
        assertThat(venta.getDispositivo()).isNull();
    }

    @Test
    void personalizacionesTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Personalizacion personalizacionBack = getPersonalizacionRandomSampleGenerator();

        venta.addPersonalizaciones(personalizacionBack);
        assertThat(venta.getPersonalizaciones()).containsOnly(personalizacionBack);

        venta.removePersonalizaciones(personalizacionBack);
        assertThat(venta.getPersonalizaciones()).doesNotContain(personalizacionBack);

        venta.personalizaciones(new HashSet<>(Set.of(personalizacionBack)));
        assertThat(venta.getPersonalizaciones()).containsOnly(personalizacionBack);

        venta.setPersonalizaciones(new HashSet<>());
        assertThat(venta.getPersonalizaciones()).doesNotContain(personalizacionBack);
    }

    @Test
    void adicionalesTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Adicional adicionalBack = getAdicionalRandomSampleGenerator();

        venta.addAdicionales(adicionalBack);
        assertThat(venta.getAdicionales()).containsOnly(adicionalBack);

        venta.removeAdicionales(adicionalBack);
        assertThat(venta.getAdicionales()).doesNotContain(adicionalBack);

        venta.adicionales(new HashSet<>(Set.of(adicionalBack)));
        assertThat(venta.getAdicionales()).containsOnly(adicionalBack);

        venta.setAdicionales(new HashSet<>());
        assertThat(venta.getAdicionales()).doesNotContain(adicionalBack);
    }
}
