package um.prog2.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static um.prog2.domain.AdicionalTestSamples.*;
import static um.prog2.domain.DispositivoTestSamples.*;
import static um.prog2.domain.VentaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import um.prog2.web.rest.TestUtil;

class AdicionalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adicional.class);
        Adicional adicional1 = getAdicionalSample1();
        Adicional adicional2 = new Adicional();
        assertThat(adicional1).isNotEqualTo(adicional2);

        adicional2.setId(adicional1.getId());
        assertThat(adicional1).isEqualTo(adicional2);

        adicional2 = getAdicionalSample2();
        assertThat(adicional1).isNotEqualTo(adicional2);
    }

    @Test
    void dispositivoTest() {
        Adicional adicional = getAdicionalRandomSampleGenerator();
        Dispositivo dispositivoBack = getDispositivoRandomSampleGenerator();

        adicional.setDispositivo(dispositivoBack);
        assertThat(adicional.getDispositivo()).isEqualTo(dispositivoBack);

        adicional.dispositivo(null);
        assertThat(adicional.getDispositivo()).isNull();
    }

    @Test
    void ventaTest() {
        Adicional adicional = getAdicionalRandomSampleGenerator();
        Venta ventaBack = getVentaRandomSampleGenerator();

        adicional.addVenta(ventaBack);
        assertThat(adicional.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getAdicionales()).containsOnly(adicional);

        adicional.removeVenta(ventaBack);
        assertThat(adicional.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getAdicionales()).doesNotContain(adicional);

        adicional.ventas(new HashSet<>(Set.of(ventaBack)));
        assertThat(adicional.getVentas()).containsOnly(ventaBack);
        assertThat(ventaBack.getAdicionales()).containsOnly(adicional);

        adicional.setVentas(new HashSet<>());
        assertThat(adicional.getVentas()).doesNotContain(ventaBack);
        assertThat(ventaBack.getAdicionales()).doesNotContain(adicional);
    }
}
