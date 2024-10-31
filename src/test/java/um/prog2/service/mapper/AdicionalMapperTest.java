package um.prog2.service.mapper;

import static um.prog2.domain.AdicionalAsserts.*;
import static um.prog2.domain.AdicionalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdicionalMapperTest {

    private AdicionalMapper adicionalMapper;

    @BeforeEach
    void setUp() {
        adicionalMapper = new AdicionalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdicionalSample1();
        var actual = adicionalMapper.toEntity(adicionalMapper.toDto(expected));
        assertAdicionalAllPropertiesEquals(expected, actual);
    }
}
