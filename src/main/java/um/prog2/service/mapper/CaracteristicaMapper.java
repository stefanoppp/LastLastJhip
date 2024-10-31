package um.prog2.service.mapper;

import org.mapstruct.*;
import um.prog2.domain.Caracteristica;
import um.prog2.domain.Dispositivo;
import um.prog2.service.dto.CaracteristicaDTO;
import um.prog2.service.dto.DispositivoDTO;

/**
 * Mapper for the entity {@link Caracteristica} and its DTO {@link CaracteristicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CaracteristicaMapper extends EntityMapper<CaracteristicaDTO, Caracteristica> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    CaracteristicaDTO toDto(Caracteristica s);

    @Named("dispositivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivoDTO toDtoDispositivoId(Dispositivo dispositivo);
}
