package um.prog2.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.prog2.domain.Opcion;
import um.prog2.repository.OpcionRepository;
import um.prog2.service.dto.OpcionDTO;
import um.prog2.service.mapper.OpcionMapper;

/**
 * Service Implementation for managing {@link um.prog2.domain.Opcion}.
 */
@Service
@Transactional
public class OpcionService {

    private static final Logger LOG = LoggerFactory.getLogger(OpcionService.class);

    private final OpcionRepository opcionRepository;

    private final OpcionMapper opcionMapper;

    public OpcionService(OpcionRepository opcionRepository, OpcionMapper opcionMapper) {
        this.opcionRepository = opcionRepository;
        this.opcionMapper = opcionMapper;
    }

    /**
     * Save a opcion.
     *
     * @param opcionDTO the entity to save.
     * @return the persisted entity.
     */
    public OpcionDTO save(OpcionDTO opcionDTO) {
        LOG.debug("Request to save Opcion : {}", opcionDTO);
        Opcion opcion = opcionMapper.toEntity(opcionDTO);
        opcion = opcionRepository.save(opcion);
        return opcionMapper.toDto(opcion);
    }

    /**
     * Update a opcion.
     *
     * @param opcionDTO the entity to save.
     * @return the persisted entity.
     */
    public OpcionDTO update(OpcionDTO opcionDTO) {
        LOG.debug("Request to update Opcion : {}", opcionDTO);
        Opcion opcion = opcionMapper.toEntity(opcionDTO);
        opcion = opcionRepository.save(opcion);
        return opcionMapper.toDto(opcion);
    }

    /**
     * Partially update a opcion.
     *
     * @param opcionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OpcionDTO> partialUpdate(OpcionDTO opcionDTO) {
        LOG.debug("Request to partially update Opcion : {}", opcionDTO);

        return opcionRepository
            .findById(opcionDTO.getId())
            .map(existingOpcion -> {
                opcionMapper.partialUpdate(existingOpcion, opcionDTO);

                return existingOpcion;
            })
            .map(opcionRepository::save)
            .map(opcionMapper::toDto);
    }

    /**
     * Get all the opcions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OpcionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Opcions");
        return opcionRepository.findAll(pageable).map(opcionMapper::toDto);
    }

    /**
     * Get one opcion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OpcionDTO> findOne(Long id) {
        LOG.debug("Request to get Opcion : {}", id);
        return opcionRepository.findById(id).map(opcionMapper::toDto);
    }

    /**
     * Delete the opcion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Opcion : {}", id);
        opcionRepository.deleteById(id);
    }
}
