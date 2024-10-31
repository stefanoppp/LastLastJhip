package um.prog2.service;

import static um.prog2.service.DataFetcherService.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import um.prog2.domain.Adicional;
import um.prog2.domain.Caracteristica;
import um.prog2.domain.Dispositivo;
import um.prog2.domain.Opcion;
import um.prog2.domain.Personalizacion;
import um.prog2.repository.AdicionalRepository;
import um.prog2.repository.CaracteristicaRepository;
import um.prog2.repository.DispositivoRepository;
import um.prog2.repository.OpcionRepository;
import um.prog2.repository.PersonalizacionRepository;

@Service
@Transactional
public class DataSyncService {

    private final Logger log = LoggerFactory.getLogger(DataSyncService.class);

    private final DispositivoRepository dispositivoRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final PersonalizacionRepository personalizacionRepository;
    private final OpcionRepository opcionRepository;
    private final AdicionalRepository adicionalRepository;

    private final DataFetcherService dataFetcherService;

    private final ObjectMapper mapper = new ObjectMapper();

    public DataSyncService(
        DispositivoRepository dispositivoRepository,
        CaracteristicaRepository caracteristicaRepository,
        PersonalizacionRepository personalizacionRepository,
        OpcionRepository opcionRepository,
        AdicionalRepository adicionalRepository,
        DataFetcherService dataFetcherService
    ) {
        this.dispositivoRepository = dispositivoRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.personalizacionRepository = personalizacionRepository;
        this.opcionRepository = opcionRepository;
        this.adicionalRepository = adicionalRepository;
        this.dataFetcherService = dataFetcherService;
    }

    @PostConstruct
    public void init() {
        log.info("Iniciando sincronización de datos al iniciar la aplicación...");
        syncData();
    }

    @Scheduled(fixedRate = 21600000)
    public void syncData() {
        log.info("Iniciando sincronización de datos programada...");
        try {
            syncDispositivos();
            syncCaracteristicas();
            syncPersonalizaciones();
            syncOpciones();
            syncAdicionales();
            log.info("Sincronización de datos completada.");
        } catch (Exception e) {
            log.error("Error en la sincronización de datos: ", e);
        }
    }

    private void syncDispositivos() throws IOException {
        List<Dispositivo> dispositivos = parseDispositivos();
        for (Dispositivo dispositivo : dispositivos) {
            log.info("Procesando dispositivo con idExterno: {}", dispositivo.getIdExterno());
            Dispositivo dispositivoExistente = dispositivoRepository.findByIdExterno(dispositivo.getIdExterno());
            if (dispositivoExistente == null) {
                dispositivoRepository.save(dispositivo);
                log.info("Dispositivo guardado: {}", dispositivo.getIdExterno());
            } else {
                log.info("Dispositivo ya existe, idExterno: {}", dispositivo.getIdExterno());
            }
        }
    }

    private void syncCaracteristicas() throws IOException {
        List<Caracteristica> caracteristicas = parseCaracteristicas();
        for (Caracteristica caracteristica : caracteristicas) {
            log.info("Procesando característica con idExterno: {}", caracteristica.getIdExterno());

            Dispositivo dispositivo = caracteristica.getDispositivo();

            // Verificar si dispositivo es nulo o no tiene idExterno
            if (dispositivo == null || dispositivo.getIdExterno() == null) {
                log.error("Dispositivo es nulo o no tiene idExterno para la característica con idExterno: {}", caracteristica.getIdExterno());
                continue;
            }

            Dispositivo dbDispositivo = dispositivoRepository.findByIdExterno(dispositivo.getIdExterno());

            if (dbDispositivo != null) {
                log.info("Dispositivo encontrado para la característica, id: {}", dbDispositivo.getId());
                caracteristica.setDispositivo(dbDispositivo);
            } else {
                log.error("No se encontró dispositivo con idExterno: {}", dispositivo.getIdExterno());
                continue; // No se puede proceder sin el dispositivo relacionado
            }

            if (caracteristica.getIdExterno() == null) {
                log.error("El campo idExterno no puede ser nulo para la característica.");
                continue; // Evitar persistencia con idExterno nulo
            }

            Caracteristica existingCaracteristica = caracteristicaRepository.findByIdExterno(caracteristica.getIdExterno());
            if (existingCaracteristica == null) {
                caracteristicaRepository.save(caracteristica);
                log.info("Característica guardada con idExterno: {}", caracteristica.getIdExterno());
            } else {
                log.info("Característica ya existe, idExterno: {}", caracteristica.getIdExterno());
            }
        }
    }

    private void syncPersonalizaciones() throws IOException {
        List<Personalizacion> personalizaciones = parsePersonalizaciones();
        for (Personalizacion personalizacion : personalizaciones) {
            log.info("Procesando personalización con idExterno: {}", personalizacion.getIdExterno());

            Dispositivo dispositivo = personalizacion.getDispositivo();

            // Verificar si dispositivo es nulo o no tiene idExterno
            if (dispositivo == null || dispositivo.getIdExterno() == null) {
                log.error("Dispositivo es nulo o no tiene idExterno para la personalización con idExterno: {}", personalizacion.getIdExterno());
                continue;
            }

            Dispositivo dbDispositivo = dispositivoRepository.findByIdExterno(dispositivo.getIdExterno());

            if (dbDispositivo != null) {
                log.info("Dispositivo encontrado para la personalización, id: {}", dbDispositivo.getId());
                personalizacion.setDispositivo(dbDispositivo);
            } else {
                log.error("No se encontró dispositivo con idExterno: {}", dispositivo.getIdExterno());
                continue; // No se puede proceder sin el dispositivo relacionado
            }

            if (personalizacion.getIdExterno() == null) {
                log.error("El campo idExterno no puede ser nulo para la personalización.");
                continue; // Evitar persistencia con idExterno nulo
            }

            Personalizacion existingPersonalizacion = personalizacionRepository.findByIdExterno(personalizacion.getIdExterno());
            if (existingPersonalizacion == null) {
                personalizacionRepository.save(personalizacion);
                log.info("Personalización guardada con idExterno: {}", personalizacion.getIdExterno());
            } else {
                log.info("Personalización ya existe, idExterno: {}", personalizacion.getIdExterno());
            }
        }
    }

    private void syncOpciones() throws IOException {
        List<Opcion> opciones = parseOpciones();
        for (Opcion opcion : opciones) {
            log.info("Procesando opción con idExterno: {}", opcion.getIdExterno());

            Personalizacion personalizacion = opcion.getPersonalizacion();

            // Verificar si la personalización es nula o no tiene idExterno
            if (personalizacion == null || personalizacion.getIdExterno() == null) {
                log.error("Personalización es nula o no tiene idExterno para la opción con idExterno: {}", opcion.getIdExterno());
                continue;
            }

            Personalizacion dbPersonalizacion = personalizacionRepository.findByIdExterno(personalizacion.getIdExterno());

            if (dbPersonalizacion != null) {
                log.info("Personalización encontrada para la opción, id: {}", dbPersonalizacion.getId());
                opcion.setPersonalizacion(dbPersonalizacion);
            } else {
                log.error("No se encontró personalización con idExterno: {}", personalizacion.getIdExterno());
                continue; // No se puede proceder sin la personalización relacionada
            }

            if (opcion.getIdExterno() == null) {
                log.error("El campo idExterno no puede ser nulo para la opción.");
                continue; // Evitar persistencia con idExterno nulo
            }

            Opcion existingOpcion = opcionRepository.findByIdExterno(opcion.getIdExterno());
            if (existingOpcion == null) {
                opcionRepository.save(opcion);
                log.info("Opción guardada con idExterno: {}", opcion.getIdExterno());
            } else {
                log.info("Opción ya existe, idExterno: {}", opcion.getIdExterno());
            }
        }
    }

    private void syncAdicionales() throws IOException {
        List<Adicional> adicionales = parseAdicionales();
        for (Adicional adicional : adicionales) {
            log.info("Procesando adicional con idExterno: {}", adicional.getIdExterno());

            Dispositivo dispositivo = adicional.getDispositivo();

            // Verificar si dispositivo es nulo o no tiene idExterno
            if (dispositivo == null || dispositivo.getIdExterno() == null) {
                log.error("Dispositivo es nulo o no tiene idExterno para el adicional con idExterno: {}", adicional.getIdExterno());
                continue;
            }

            Dispositivo dbDispositivo = dispositivoRepository.findByIdExterno(dispositivo.getIdExterno());

            if (dbDispositivo != null) {
                log.info("Dispositivo encontrado para el adicional, id: {}", dbDispositivo.getId());
                adicional.setDispositivo(dbDispositivo);
            } else {
                log.error("No se encontró dispositivo con idExterno: {}", dispositivo.getIdExterno());
                continue; // No se puede proceder sin el dispositivo relacionado
            }

            if (adicional.getIdExterno() == null) {
                log.error("El campo idExterno no puede ser nulo para el adicional.");
                continue; // Evitar persistencia con idExterno nulo
            }

            Adicional existingAdicional = adicionalRepository.findByIdExterno(adicional.getIdExterno());
            if (existingAdicional == null) {
                adicionalRepository.save(adicional);
                log.info("Adicional guardado con idExterno: {}", adicional.getIdExterno());
            } else {
                log.info("Adicional ya existe, idExterno: {}", adicional.getIdExterno());
            }
        }
    }

    // Métodos de parseo

    private List<Dispositivo> parseDispositivos() throws IOException {
        String jsonData = DataFetcherService.getDispositivos();
        return mapper.readValue(jsonData, new TypeReference<List<Dispositivo>>() {});
    }

    private List<Caracteristica> parseCaracteristicas() throws IOException {
        String jsonData = dataFetcherService.getCaracteristicas();
        return mapper.readValue(jsonData, new TypeReference<List<Caracteristica>>() {});
    }

    private List<Personalizacion> parsePersonalizaciones() throws IOException {
        String jsonData = dataFetcherService.getPersonalizaciones();
        return mapper.readValue(jsonData, new TypeReference<List<Personalizacion>>() {});
    }

    private List<Opcion> parseOpciones() throws IOException {
        String jsonData = dataFetcherService.getOpciones();
        return mapper.readValue(jsonData, new TypeReference<List<Opcion>>() {});
    }

    private List<Adicional> parseAdicionales() throws IOException {
        String jsonData = dataFetcherService.getAdicionales();
        return mapper.readValue(jsonData, new TypeReference<List<Adicional>>() {});
    }
}
