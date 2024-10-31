package um.prog2.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import um.prog2.domain.Dispositivo;
import um.prog2.domain.Personalizacion;
import um.prog2.domain.Venta;
import um.prog2.service.dto.DispositivoDTO;
import um.prog2.service.dto.PersonalizacionDTO;
import um.prog2.service.dto.VentaDTO;

/**
 * Mapper for the entity {@link Personalizacion} and its DTO {@link PersonalizacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonalizacionMapper extends EntityMapper<PersonalizacionDTO, Personalizacion> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    @Mapping(target = "ventas", source = "ventas", qualifiedByName = "ventaIdSet")
    PersonalizacionDTO toDto(Personalizacion s);

    @Mapping(target = "ventas", ignore = true)
    @Mapping(target = "removeVenta", ignore = true)
    Personalizacion toEntity(PersonalizacionDTO personalizacionDTO);

    @Named("dispositivoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DispositivoDTO toDtoDispositivoId(Dispositivo dispositivo);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);

    @Named("ventaIdSet")
    default Set<VentaDTO> toDtoVentaIdSet(Set<Venta> venta) {
        return venta.stream().map(this::toDtoVentaId).collect(Collectors.toSet());
    }
}
