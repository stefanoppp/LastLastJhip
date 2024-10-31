package um.prog2.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static um.prog2.domain.DispositivoTestSamples.*;
import static um.prog2.domain.PersonalizacionTestSamples.*;
import static um.prog2.domain.VentaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import um.prog2.web.rest.TestUtil;

class PersonalizacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personalizacion.class);
        Personalizacion personalizacion1 = getPersonalizacionSample1();
        Personalizacion personalizacion2 = new Personalizacion();
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);

        personalizacion2.setId(personalizacion1.getId());
        assertThat(personalizacion1).isEqualTo(personalizacion2);

        personalizacion2 = getPersonalizacionSample2();
        assertThat(personalizacion1).isNotEqualTo(personalizacion2);
    }

    @Test
    void dispositivoTest() {
        Personalizacion personalizacion = getPersonalizacionRandomSampleGenerator();
        Dispositivo dispositivoBack = getDispositivoRandomSampleGenerator();

        personalizacion.setDispositivo(dispositivoBack);
        assertThat(personalizacion.getDispositivo()).isEqualTo(dispositivoBack);

        personalizacion.dispositivo(null);
        assertThat(personalizacion.getDispositivo()).isNull();
    }

    @Test
    void ventaTest() {
        Personalizacion personalizacion = getPersonalizacionRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        personalizacion.addVenta(ventaBack);
        assertThat(personalizacion.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getPersonalizaciones()).containsOnly(personalizacion);

        personalizacion.removeVenta(ventaBack);
        assertThat(personalizacion.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getPersonalizaciones()).doesNotContain(personalizacion);

        personalizacion.ventas(new HashSet<>(Set.of(ventaBack)));
        assertThat(personalizacion.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getPersonalizaciones()).containsOnly(personalizacion);

        personalizacion.setVentas(new HashSet<>());
        assertThat(personalizacion.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getPersonalizaciones()).doesNotContain(personalizacion);
    }
}
