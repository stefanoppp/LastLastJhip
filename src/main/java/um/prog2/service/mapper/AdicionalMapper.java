package um.prog2.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import um.prog2.domain.Adicional;
import um.prog2.domain.Dispositivo;
import um.prog2.domain.Venta;
import um.prog2.service.dto.AdicionalDTO;
import um.prog2.service.dto.DispositivoDTO;
import um.prog2.service.dto.VentaDTO;

/**
 * Mapper for the entity {@link Adicional} and its DTO {@link AdicionalDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdicionalMapper extends EntityMapper<AdicionalDTO, Adicional> {
    @Mapping(target = "dispositivo", source = "dispositivo", qualifiedByName = "dispositivoId")
    @Mapping(target = "ventas", source = "ventas", qualifiedByName = "ventaIdSet")
    AdicionalDTO toDto(Adicional s);

    @Mapping(target = "ventas", ignore = true)
    @Mapping(target = "removeVenta", ignore = true)
    Adicional toEntity(AdicionalDTO adicionalDTO);

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
