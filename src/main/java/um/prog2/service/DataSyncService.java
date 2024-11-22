package um.prog2.service;

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
    private final ObjectMapper mapper;

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
        this.mapper = new ObjectMapper();
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
        List<Dispositivo> dispositivos = mapper.readValue(dataFetcherService.getDispositivos(), new TypeReference<List<Dispositivo>>() {});
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
        List<Caracteristica> caracteristicas = mapper.readValue(dataFetcherService.getCaracteristicas(), new TypeReference<List<Caracteristica>>() {});
        for (Caracteristica caracteristica : caracteristicas) {
            log.info("Procesando característica con idExterno: {}", caracteristica.getIdExterno());
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
        List<Personalizacion> personalizaciones = mapper.readValue(dataFetcherService.getPersonalizaciones(), new TypeReference<List<Personalizacion>>() {});
    }

    private void syncOpciones() throws IOException {
        List<Opcion> opciones = mapper.readValue(dataFetcherService.getOpciones(), new TypeReference<List<Opcion>>() {});
    }

    private void syncAdicionales() throws IOException {
        List<Adicional> adicionales = mapper.readValue(dataFetcherService.getAdicionales(), new TypeReference<List<Adicional>>() {});
    }
}
