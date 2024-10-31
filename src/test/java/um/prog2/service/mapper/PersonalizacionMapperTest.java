package um.prog2.service.mapper;

import static um.prog2.domain.PersonalizacionAsserts.*;
import static um.prog2.domain.PersonalizacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonalizacionMapperTest {

    private PersonalizacionMapper personalizacionMapper;

    @BeforeEach
    void setUp() {
        personalizacionMapper = new PersonalizacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonalizacionSample1();
        var actual = personalizacionMapper.toEntity(personalizacionMapper.toDto(expected));
        assertPersonalizacionAllPropertiesEquals(expected, actual);
    }
}
