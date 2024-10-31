package um.prog2.service.mapper;

import static um.prog2.domain.VentaAsserts.*;
import static um.prog2.domain.VentaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentaMapperTest {

    private VentaMapper ventaMapper;

    @BeforeEach
    void setUp() {
        ventaMapper = new VentaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVentaSample1();
        var actual = ventaMapper.toEntity(ventaMapper.toDto(expected));
        assertVentaAllPropertiesEquals(expected, actual);
    }
}
