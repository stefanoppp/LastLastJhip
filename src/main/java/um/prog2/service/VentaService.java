package um.prog2.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import java.time.format.DateTimeFormatter;

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

    @Value("${apiconfig.url-venta}")
    private String ventaUrl;

    @Value("${apiconfig.token}")
    private String apiToken;

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

        // Modificar y enviar el JSON a otra URL
        modifyAndSendVenta(ventaDTO);

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

    /**
     * Modifica el JSON recibido para cumplir con el formato requerido y envía la venta a otra URL usando un token de autenticación.
     *
     * @param ventaDTO la entidad que se debe modificar y enviar.
     */
    public void modifyAndSendVenta(VentaDTO ventaDTO) {
        LOG.debug("Request to modify and send Venta : {}", ventaDTO);

        try {
            String formattedJson = formatVentaToJson(ventaDTO);
            LOG.debug("Formatted JSON to send: {}", formattedJson);

            // Enviar la venta formateada a la URL destino
            sendVentaToTargetUrl(formattedJson);
        } catch (IOException e) {
            LOG.error("Error al formatear y enviar la venta: ", e);
        }
    }

    /**
     * Formatea la venta recibida a un JSON con el formato específico requerido, incluyendo los ids externos.
     */
    private String formatVentaToJson(VentaDTO ventaDTO) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Creando un nuevo objeto que representará la estructura deseada con ids externos
        var formattedVenta = new Object() {
            public Long idDispositivo = ventaDTO.getDispositivo().getIdExterno();
            public Set<Object> personalizaciones = new HashSet<>();
            public Set<Object> adicionales = new HashSet<>();
            public Float precioFinal = ventaDTO.getPrecioFinal() != null ? ventaDTO.getPrecioFinal().floatValue() : 0.0f;
            public String fechaVenta = ventaDTO.getFechaVenta().format(DateTimeFormatter.ISO_INSTANT);
        };

        // Añadiendo personalizaciones con id externo al formato deseado
        for (PersonalizacionDTO personalizacionDTO : ventaDTO.getPersonalizaciones()) {
            var formattedPersonalizacion = new Object() {
                public Long id = personalizacionDTO.getIdExterno();
            };
            formattedVenta.personalizaciones.add(formattedPersonalizacion);
        }

        // Añadiendo adicionales con id externo al formato deseado
        for (AdicionalDTO adicionalDTO : ventaDTO.getAdicionales()) {
            var formattedAdicional = new Object() {
                public Long id = adicionalDTO.getIdExterno();
                public Float precio = adicionalDTO.getPrecio() != null ? adicionalDTO.getPrecio().floatValue() : 0.0f;
            };
            formattedVenta.adicionales.add(formattedAdicional);
        }

        // Convertir a JSON String
        return mapper.writeValueAsString(formattedVenta);
    }

    /**
     * Envía la venta formateada a la URL objetivo usando un token de autenticación.
     */
    private void sendVentaToTargetUrl(String formattedJson) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(formattedJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                ventaUrl, // Utilizando la URL de venta desde las variables de entorno
                HttpMethod.POST,
                requestEntity,
                String.class
            );
            LOG.info("Venta enviada exitosamente, respuesta: {}", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOG.error("Error al enviar la venta al target URL. Código de estado: {}, Cuerpo de la respuesta: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            LOG.error("Error al enviar la venta al target URL: ", e);
        }
    }
}
