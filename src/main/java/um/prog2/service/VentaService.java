package um.prog2.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.prog2.domain.Adicional;
import um.prog2.domain.Dispositivo;
import um.prog2.domain.Personalizacion;
import um.prog2.domain.Venta;
import um.prog2.repository.AdicionalRepository;
import um.prog2.repository.DispositivoRepository;
import um.prog2.repository.PersonalizacionRepository;
import um.prog2.repository.VentaRepository;
import um.prog2.service.dto.AdicionalDTO;
import um.prog2.service.dto.PersonalizacionDTO;
import um.prog2.service.dto.VentaDTO;
import um.prog2.service.mapper.AdicionalMapper;
import um.prog2.service.mapper.DispositivoMapper;
import um.prog2.service.mapper.PersonalizacionMapper;
import um.prog2.service.mapper.VentaMapper;

/**
 * Service Implementation for managing {@link um.prog2.domain.Venta}.
 */
@Service
@Transactional
public class VentaService {

    private static final Logger LOG = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final DispositivoRepository dispositivoRepository;
    private final PersonalizacionRepository personalizacionRepository;
    private final AdicionalRepository adicionalRepository;
    private final DispositivoMapper dispositivoMapper;
    private final PersonalizacionMapper personalizacionMapper;
    private final AdicionalMapper adicionalMapper;

    public VentaService(
        VentaRepository ventaRepository,
        VentaMapper ventaMapper,
        DispositivoRepository dispositivoRepository,
        PersonalizacionRepository personalizacionRepository,
        AdicionalRepository adicionalRepository,
        DispositivoMapper dispositivoMapper,
        PersonalizacionMapper personalizacionMapper,
        AdicionalMapper adicionalMapper
    ) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.dispositivoRepository = dispositivoRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.adicionalRepository = adicionalRepository;
        this.dispositivoMapper = dispositivoMapper;
        this.personalizacionMapper = personalizacionMapper;
        this.adicionalMapper = adicionalMapper;
    }

    /**
     * Save a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    public VentaDTO save(VentaDTO ventaDTO) {
        LOG.debug("Request to save Venta : {}", ventaDTO);

        // Validar y obtener el Dispositivo por idExterno
        if (ventaDTO.getDispositivo() != null && ventaDTO.getDispositivo().getIdExterno() != null) {
            Dispositivo dispositivo = dispositivoRepository.findByIdExterno(ventaDTO.getDispositivo().getIdExterno());
            if (dispositivo == null) {
                throw new IllegalArgumentException("Dispositivo con idExterno " + ventaDTO.getDispositivo().getIdExterno() + " no existe.");
            }
            // Convertir entidad a DTO antes de asignar
            ventaDTO.setDispositivo(dispositivoMapper.toDto(dispositivo));
        } else {
            throw new IllegalArgumentException("El campo idExterno de Dispositivo no puede ser nulo.");
        }

        // Validar y obtener las Personalizaciones por idExterno
        Set<PersonalizacionDTO> personalizacionDTOs = new HashSet<>();
        if (ventaDTO.getPersonalizaciones() != null) {
            for (var personalizacionDTO : ventaDTO.getPersonalizaciones()) {
                if (personalizacionDTO.getIdExterno() != null) {
                    Personalizacion personalizacion = personalizacionRepository.findByIdExterno(personalizacionDTO.getIdExterno());
                    if (personalizacion == null) {
                        throw new IllegalArgumentException("Personalizacion con idExterno " + personalizacionDTO.getIdExterno() + " no existe.");
                    }
                    // Convertir entidad a DTO antes de asignar
                    personalizacionDTOs.add(personalizacionMapper.toDto(personalizacion));
                } else {
                    throw new IllegalArgumentException("El campo idExterno de Personalizacion no puede ser nulo.");
                }
            }
        }
        ventaDTO.setPersonalizaciones(personalizacionDTOs);

        // Validar y obtener los Adicionales por idExterno
        Set<AdicionalDTO> adicionalDTOs = new HashSet<>();
        if (ventaDTO.getAdicionales() != null) {
            for (var adicionalDTO : ventaDTO.getAdicionales()) {
                if (adicionalDTO.getIdExterno() != null) {
                    Adicional adicional = adicionalRepository.findByIdExterno(adicionalDTO.getIdExterno());
                    if (adicional == null) {
                        throw new IllegalArgumentException("Adicional con idExterno " + adicionalDTO.getIdExterno() + " no existe.");
                    }
                    // Convertir entidad a DTO antes de asignar
                    adicionalDTOs.add(adicionalMapper.toDto(adicional));
                } else {
                    throw new IllegalArgumentException("El campo idExterno de Adicional no puede ser nulo.");
                }
            }
        }
        ventaDTO.setAdicionales(adicionalDTOs);

        // Convertir VentaDTO a Venta y guardar
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    /**
     * Update a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    public VentaDTO update(VentaDTO ventaDTO) {
        LOG.debug("Request to update Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    /**
     * Partially update a venta.
     *
     * @param ventaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        LOG.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepository
            .findById(ventaDTO.getId())
            .map(existingVenta -> {
                ventaMapper.partialUpdate(existingVenta, ventaDTO);
                return existingVenta;
            })
            .map(ventaRepository::save)
            .map(ventaMapper::toDto);
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VentaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable).map(ventaMapper::toDto);
    }

    /**
     * Get all the ventas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VentaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable).map(ventaMapper::toDto);
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VentaDTO> findOne(Long id) {
        LOG.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id).map(ventaMapper::toDto);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
