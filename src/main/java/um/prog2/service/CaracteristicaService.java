package um.prog2.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.prog2.domain.Caracteristica;
import um.prog2.repository.CaracteristicaRepository;
import um.prog2.service.dto.CaracteristicaDTO;
import um.prog2.service.mapper.CaracteristicaMapper;

/**
 * Service Implementation for managing {@link um.prog2.domain.Caracteristica}.
 */
@Service
@Transactional
public class CaracteristicaService {

    private static final Logger LOG = LoggerFactory.getLogger(CaracteristicaService.class);

    private final CaracteristicaRepository caracteristicaRepository;

    private final CaracteristicaMapper caracteristicaMapper;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository, CaracteristicaMapper caracteristicaMapper) {
        this.caracteristicaRepository = caracteristicaRepository;
        this.caracteristicaMapper = caracteristicaMapper;
    }

    /**
     * Save a caracteristica.
     *
     * @param caracteristicaDTO the entity to save.
     * @return the persisted entity.
     */
    public CaracteristicaDTO save(CaracteristicaDTO caracteristicaDTO) {
        LOG.debug("Request to save Caracteristica : {}", caracteristicaDTO);
        Caracteristica caracteristica = caracteristicaMapper.toEntity(caracteristicaDTO);
        caracteristica = caracteristicaRepository.save(caracteristica);
        return caracteristicaMapper.toDto(caracteristica);
    }

    /**
     * Update a caracteristica.
     *
     * @param caracteristicaDTO the entity to save.
     * @return the persisted entity.
     */
    public CaracteristicaDTO update(CaracteristicaDTO caracteristicaDTO) {
        LOG.debug("Request to update Caracteristica : {}", caracteristicaDTO);
        Caracteristica caracteristica = caracteristicaMapper.toEntity(caracteristicaDTO);
        caracteristica = caracteristicaRepository.save(caracteristica);
        return caracteristicaMapper.toDto(caracteristica);
    }

    /**
     * Partially update a caracteristica.
     *
     * @param caracteristicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CaracteristicaDTO> partialUpdate(CaracteristicaDTO caracteristicaDTO) {
        LOG.debug("Request to partially update Caracteristica : {}", caracteristicaDTO);

        return caracteristicaRepository
            .findById(caracteristicaDTO.getId())
            .map(existingCaracteristica -> {
                caracteristicaMapper.partialUpdate(existingCaracteristica, caracteristicaDTO);

                return existingCaracteristica;
            })
            .map(caracteristicaRepository::save)
            .map(caracteristicaMapper::toDto);
    }

    /**
     * Get all the caracteristicas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CaracteristicaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Caracteristicas");
        return caracteristicaRepository.findAll(pageable).map(caracteristicaMapper::toDto);
    }

    /**
     * Get one caracteristica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CaracteristicaDTO> findOne(Long id) {
        LOG.debug("Request to get Caracteristica : {}", id);
        return caracteristicaRepository.findById(id).map(caracteristicaMapper::toDto);
    }

    /**
     * Delete the caracteristica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Caracteristica : {}", id);
        caracteristicaRepository.deleteById(id);
    }
}
