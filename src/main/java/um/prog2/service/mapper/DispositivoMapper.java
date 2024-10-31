package um.prog2.service.mapper;

import org.mapstruct.*;
import um.prog2.domain.Dispositivo;
import um.prog2.service.dto.DispositivoDTO;

/**
 * Mapper for the entity {@link Dispositivo} and its DTO {@link DispositivoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DispositivoMapper extends EntityMapper<DispositivoDTO, Dispositivo> {}
