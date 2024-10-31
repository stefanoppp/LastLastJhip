package um.prog2.service.mapper;

import static um.prog2.domain.DispositivoAsserts.*;
import static um.prog2.domain.DispositivoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispositivoMapperTest {

    private DispositivoMapper dispositivoMapper;

    @BeforeEach
    void setUp() {
        dispositivoMapper = new DispositivoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDispositivoSample1();
        var actual = dispositivoMapper.toEntity(dispositivoMapper.toDto(expected));
        assertDispositivoAllPropertiesEquals(expected, actual);
    }
}
