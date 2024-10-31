package um.prog2.service.mapper;

import org.mapstruct.*;
import um.prog2.domain.Opcion;
import um.prog2.domain.Personalizacion;
import um.prog2.service.dto.OpcionDTO;
import um.prog2.service.dto.PersonalizacionDTO;

/**
 * Mapper for the entity {@link Opcion} and its DTO {@link OpcionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OpcionMapper extends EntityMapper<OpcionDTO, Opcion> {
    @Mapping(target = "personalizacion", source = "personalizacion", qualifiedByName = "personalizacionId")
    OpcionDTO toDto(Opcion s);

    @Named("personalizacionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonalizacionDTO toDtoPersonalizacionId(Personalizacion personalizacion);
}
