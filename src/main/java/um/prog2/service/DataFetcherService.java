package um.prog2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class DataFetcherService {

    @Value("${apiconfig.url}")
    private String apiUrl;

    @Value("${apiconfig.token}")
    private String apiToken;

    private final ObjectMapper mapper = new ObjectMapper();

    public String getDataFromApi() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            request.addHeader("Authorization", "Bearer " + apiToken);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity);
            }
        }
    }

    public String getDispositivos() throws IOException {
        String jsonResponse = getDataFromApi();
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            int id = device.get("id").asInt();

            if (!processedIds.contains(id)) {
                processedIds.add(id);
                ObjectNode simpleDevice = mapper.createObjectNode();
                simpleDevice.put("idExterno", id);
                simpleDevice.put("codigo", device.get("codigo").asText());
                simpleDevice.put("nombre", device.get("nombre").asText());
                simpleDevice.put("descripcion", device.get("descripcion").asText());
                simpleDevice.put("precioBase", device.get("precioBase").asDouble());
                simpleDevice.put("moneda", device.get("moneda").asText());
                resultArray.add(simpleDevice);
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public String getCaracteristicas() throws IOException {
        String jsonResponse = getDataFromApi();
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            int dispositivoId = device.get("id").asInt();
            ArrayNode caracteristicas = (ArrayNode) device.get("caracteristicas");

            for (int j = 0; j < caracteristicas.size(); j++) {
                ObjectNode caracteristica = (ObjectNode) caracteristicas.get(j);
                int caracteristicaId = caracteristica.get("id").asInt();

                if (!processedIds.contains(caracteristicaId)) {
                    processedIds.add(caracteristicaId);
                    caracteristica.put("idExterno", caracteristicaId);

                    ObjectNode dispositivo = mapper.createObjectNode();
                    dispositivo.put("idExterno", dispositivoId);
                    dispositivo.put("nombre", device.get("nombre").asText());
                    caracteristica.set("dispositivo", dispositivo);

                    resultArray.add(caracteristica);
                }
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public String getPersonalizaciones() throws IOException {
        String jsonResponse = getDataFromApi();
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            int dispositivoId = device.get("id").asInt();
            ArrayNode personalizaciones = (ArrayNode) device.get("personalizaciones");

            for (int j = 0; j < personalizaciones.size(); j++) {
                ObjectNode personalizacion = (ObjectNode) personalizaciones.get(j);
                int personalizacionId = personalizacion.get("id").asInt();

                if (!processedIds.contains(personalizacionId)) {
                    processedIds.add(personalizacionId);
                    personalizacion.put("idExterno", personalizacionId);

                    ObjectNode dispositivo = mapper.createObjectNode();
                    dispositivo.put("idExterno", dispositivoId);
                    dispositivo.put("nombre", device.get("nombre").asText());
                    personalizacion.set("dispositivo", dispositivo);

                    resultArray.add(personalizacion);
                }
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public String getOpciones() throws IOException {
        String jsonResponse = getDataFromApi();
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            ArrayNode personalizaciones = (ArrayNode) device.get("personalizaciones");

            for (int j = 0; j < personalizaciones.size(); j++) {
                ObjectNode personalizacion = (ObjectNode) personalizaciones.get(j);
                int personalizacionId = personalizacion.get("id").asInt();
                ArrayNode opciones = (ArrayNode) personalizacion.get("opciones");

                for (int k = 0; k < opciones.size(); k++) {
                    ObjectNode opcion = (ObjectNode) opciones.get(k);
                    int opcionId = opcion.get("id").asInt();

                    if (!processedIds.contains(opcionId)) {
                        processedIds.add(opcionId);
                        opcion.put("idExterno", opcionId);

                        ObjectNode personalizacionNode = mapper.createObjectNode();
                        personalizacionNode.put("idExterno", personalizacionId);
                        personalizacionNode.put("nombre", personalizacion.get("nombre").asText());
                        opcion.set("personalizacion", personalizacionNode);

                        resultArray.add(opcion);
                    }
                }
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }

    public String getAdicionales() throws IOException {
        String jsonResponse = getDataFromApi();
        ArrayNode originalArray = (ArrayNode) mapper.readTree(jsonResponse);
        ArrayNode resultArray = mapper.createArrayNode();
        Set<Integer> processedIds = new HashSet<>();

        for (int i = 0; i < originalArray.size(); i++) {
            ObjectNode device = (ObjectNode) originalArray.get(i);
            int dispositivoId = device.get("id").asInt();
            ArrayNode adicionales = (ArrayNode) device.get("adicionales");

            for (int j = 0; j < adicionales.size(); j++) {
                ObjectNode adicional = (ObjectNode) adicionales.get(j);
                int adicionalId = adicional.get("id").asInt();

                if (!processedIds.contains(adicionalId)) {
                    processedIds.add(adicionalId);
                    adicional.put("idExterno", adicionalId);

                    ObjectNode dispositivo = mapper.createObjectNode();
                    dispositivo.put("idExterno", dispositivoId);
                    dispositivo.put("nombre", device.get("nombre").asText());
                    adicional.set("dispositivo", dispositivo);

                    resultArray.add(adicional);
                }
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
    }
}
