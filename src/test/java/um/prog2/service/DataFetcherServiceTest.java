package um.prog2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class DataFetcherServiceTest {

    @Spy
    @InjectMocks
    private DataFetcherService dataFetcherService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetDispositivos_Success() throws IOException {
        // Mockear la respuesta del método getDataFromApi para devolver un JSON válido con dispositivos
        String mockJsonResponse = "[" +
            "{\"id\": 1502, \"idExterno\": 1, \"codigo\": \"NTB01\", \"nombre\": \"Lenovo IdeaPad 1 Laptop\", " +
            "\"descripcion\": \"Lenovo IdeaPad 1 Laptop, 15.6\\\" FHD Display, AMD Ryzen 5 5500U, 8GB RAM, 512GB SSD, Windows 11 Home, 720p Camera w/Privacy Shutter, Smart Noise Cancelling, Cloud Grey\", " +
            "\"precioBase\": 450.00, \"moneda\": \"USD\"}," +
            "{\"id\": 1503, \"idExterno\": 2, \"codigo\": \"NTB02\", \"nombre\": \"MSI Stealth 18 AI Studio\", " +
            "\"descripcion\": \"MSI Stealth 18 AI Studio 18\\\" 240Hz QHD+ Gaming Laptop: Intel Ultra 9-185H, NVIDIA Geforce RTX 4080, 32GB DDR5, 1TB NVMe SSD\", " +
            "\"precioBase\": 2899.00, \"moneda\": \"USD\"}" +
            "]";

        // Espiar el método privado getDataFromApi para devolver una respuesta simulada
        doReturn(mockJsonResponse).when(dataFetcherService).getDataFromApi();

        // Llamar al método getDispositivos y verificar el resultado
        String result = dataFetcherService.getDispositivos();

        // Parsear el resultado JSON
        JsonNode resultNode = objectMapper.readTree(result);
        assertNotNull(resultNode, "El resultado no debería ser nulo.");
        assertEquals(2, resultNode.size(), "Deberían haberse devuelto dos dispositivos.");

        // Verificar dispositivos
        JsonNode dispositivo1 = resultNode.get(0);
        assertEquals(1, dispositivo1.get("idExterno").asInt(), "El idExterno del primer dispositivo debería ser 1.");
        assertEquals("NTB01", dispositivo1.get("codigo").asText(), "El código del primer dispositivo debería ser NTB01.");

        JsonNode dispositivo2 = resultNode.get(1);
        assertEquals(2, dispositivo2.get("idExterno").asInt(), "El idExterno del segundo dispositivo debería ser 2.");
        assertEquals("NTB02", dispositivo2.get("codigo").asText(), "El código del segundo dispositivo debería ser NTB02.");
    }
    @Test
    public void testGetCaracteristicas_Success() throws IOException {
        // Mockear la respuesta del método getDataFromApi para devolver un JSON simple con características
        String mockJsonResponse = "[" +
            "{\"id\": 1507, \"idExterno\": 1, \"nombre\": \"Pantalla\", \"descripcion\": \"15.6\\\" FHD Display\", " +
            "\"dispositivo\": {\"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}," +
            "{\"id\": 1508, \"idExterno\": 2, \"nombre\": \"Camara\", \"descripcion\": \"720p Camera w/Privacy Shutter\", " +
            "\"dispositivo\": {\"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}" +
            "]";

        // Simplificar la configuración de mock para evitar problemas de Mockito
        doReturn(mockJsonResponse).when(dataFetcherService).getDataFromApi();

        // Llamar al método y verificar que el resultado no sea nulo o vacío
        String result = dataFetcherService.getCaracteristicas();
        JsonNode resultNode = objectMapper.readTree(result);

        assertNotNull(resultNode, "El resultado no debería ser nulo.");
        assertFalse(resultNode.isEmpty(), "El resultado no debería estar vacío.");

        // Verificar que cada nodo contenga los campos básicos
        for (JsonNode caracteristica : resultNode) {
            assertNotNull(caracteristica.get("idExterno"), "El campo 'idExterno' no debería ser nulo.");
            assertNotNull(caracteristica.get("nombre"), "El campo 'nombre' no debería ser nulo.");
            assertNotNull(caracteristica.get("descripcion"), "El campo 'descripcion' no debería ser nulo.");

            // Verificar el dispositivo anidado
            JsonNode dispositivo = caracteristica.get("dispositivo");
            assertNotNull(dispositivo, "El dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("idExterno"), "El campo 'idExterno' del dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("nombre"), "El campo 'nombre' del dispositivo no debería ser nulo.");
        }
    }
    @Test
    public void testGetPersonalizaciones_Success() throws IOException {
        // Mockear la respuesta del método getDataFromApi para devolver un JSON válido con personalizaciones
        String mockJsonResponse = "[" +
            "{\"id\": 1506, \"idExterno\": 1, \"nombre\": \"CPU\", \"descripcion\": \"Procesadores Disponibles\", " +
            "\"dispositivo\": {\"id\": 1502, \"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}," +
            "{\"id\": 1507, \"idExterno\": 2, \"nombre\": \"Memoria\", \"descripcion\": \"Memorias Disponibles\", " +
            "\"dispositivo\": {\"id\": 1502, \"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}," +
            "{\"id\": 1508, \"idExterno\": 5, \"nombre\": \"Video\", \"descripcion\": \"Video Disponible\", " +
            "\"dispositivo\": {\"id\": 1502, \"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}," +
            "{\"id\": 1509, \"idExterno\": 3, \"nombre\": \"CPU\", \"descripcion\": \"Procesadores Disponibles\", " +
            "\"dispositivo\": {\"id\": 1503, \"idExterno\": 2, \"nombre\": \"MSI Stealth 18 AI Studio\"}}," +
            "{\"id\": 1510, \"idExterno\": 4, \"nombre\": \"Memoria\", \"descripcion\": \"Memorias Disponibles\", " +
            "\"dispositivo\": {\"id\": 1503, \"idExterno\": 2, \"nombre\": \"MSI Stealth 18 AI Studio\"}}," +
            "{\"id\": 1511, \"idExterno\": 6, \"nombre\": \"Video\", \"descripcion\": \"Video Disponible\", " +
            "\"dispositivo\": {\"id\": 1503, \"idExterno\": 2, \"nombre\": \"MSI Stealth 18 AI Studio\"}}" +
            "]";

        // Espiar el método privado getDataFromApi para devolver una respuesta simulada
        doReturn(mockJsonResponse).when(dataFetcherService).getDataFromApi();

        // Llamar al método getPersonalizaciones y verificar el resultado
        String result = dataFetcherService.getPersonalizaciones();

        // Parsear el resultado JSON
        JsonNode resultNode = objectMapper.readTree(result);
        assertNotNull(resultNode, "El resultado no debería ser nulo.");
        assertFalse(resultNode.isEmpty(), "El resultado no debería estar vacío.");

        // Verificar cada personalización y sus atributos
        for (JsonNode personalizacion : resultNode) {
            assertNotNull(personalizacion.get("idExterno"), "El campo 'idExterno' no debería ser nulo.");
            assertNotNull(personalizacion.get("nombre"), "El campo 'nombre' no debería ser nulo.");
            assertNotNull(personalizacion.get("descripcion"), "El campo 'descripcion' no debería ser nulo.");

            // Verificar que el campo 'nombre' y 'descripcion' no estén vacíos
            String nombre = personalizacion.get("nombre").asText();
            String descripcion = personalizacion.get("descripcion").asText();
            assertFalse(nombre.isEmpty(), "El campo 'nombre' no debería estar vacío.");
            assertFalse(descripcion.isEmpty(), "El campo 'descripcion' no debería estar vacío.");

            // Verificar el dispositivo anidado
            JsonNode dispositivo = personalizacion.get("dispositivo");
            assertNotNull(dispositivo, "El dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("idExterno"), "El campo 'idExterno' del dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("nombre"), "El campo 'nombre' del dispositivo no debería ser nulo.");

            // Verificar que el campo 'nombre' del dispositivo no esté vacío
            String dispositivoNombre = dispositivo.get("nombre").asText();
            assertFalse(dispositivoNombre.isEmpty(), "El campo 'nombre' del dispositivo no debería estar vacío.");
        }
    }
    @Test
    public void testGetOpciones_Success() throws IOException {
        // Mockear la respuesta del método getDataFromApi para devolver un JSON válido con opciones
        String mockJsonResponse = "[" +
            "{\"id\": 1512, \"idExterno\": 1, \"codigo\": \"PROC01\", \"nombre\": \"Ryzen 5 5500U\", \"descripcion\": \"Procesador 1.8 GHz - 6(12) Cores\", \"precioAdicional\": 0.00, " +
            "\"personalizacion\": {\"id\": 1506, \"idExterno\": 1, \"nombre\": \"CPU\", \"descripcion\": \"Procesadores Disponibles\"}}," +
            "{\"id\": 1513, \"idExterno\": 2, \"codigo\": \"PROC02\", \"nombre\": \"Ryzen 5 5700U\", \"descripcion\": \"Procesador 2.1 GHz - 8(16) Cores\", \"precioAdicional\": 120.00, " +
            "\"personalizacion\": {\"id\": 1506, \"idExterno\": 1, \"nombre\": \"CPU\", \"descripcion\": \"Procesadores Disponibles\"}}," +
            "{\"id\": 1514, \"idExterno\": 5, \"codigo\": \"MEM01\", \"nombre\": \"DDR4-8\", \"descripcion\": \"Memoria DDR4 - 8GB\", \"precioAdicional\": 0.00, " +
            "\"personalizacion\": {\"id\": 1507, \"idExterno\": 2, \"nombre\": \"Memoria\", \"descripcion\": \"Memorias Disponibles\"}}" +
            "]";

        // Espiar el método privado getDataFromApi para devolver una respuesta simulada
        doReturn(mockJsonResponse).when(dataFetcherService).getDataFromApi();

        // Llamar al método getOpciones y verificar el resultado
        String result = dataFetcherService.getOpciones();

        // Parsear el resultado JSON
        JsonNode resultNode = objectMapper.readTree(result);
        assertNotNull(resultNode, "El resultado no debería ser nulo.");
        assertFalse(resultNode.isEmpty(), "El resultado no debería estar vacío.");

        // Verificar cada opción y sus atributos
        for (JsonNode opcion : resultNode) {
            assertNotNull(opcion.get("idExterno"), "El campo 'idExterno' no debería ser nulo.");
            assertNotNull(opcion.get("nombre"), "El campo 'nombre' no debería ser nulo.");
            assertNotNull(opcion.get("descripcion"), "El campo 'descripcion' no debería ser nulo.");
            assertNotNull(opcion.get("codigo"), "El campo 'codigo' no debería ser nulo.");
            assertNotNull(opcion.get("precioAdicional"), "El campo 'precioAdicional' no debería ser nulo.");

            // Verificar que los campos de nombre, descripción y código no estén vacíos
            assertFalse(opcion.get("nombre").asText().isEmpty(), "El campo 'nombre' no debería estar vacío.");
            assertFalse(opcion.get("descripcion").asText().isEmpty(), "El campo 'descripcion' no debería estar vacío.");
            assertFalse(opcion.get("codigo").asText().isEmpty(), "El campo 'codigo' no debería estar vacío.");

            // Verificar la personalización anidada
            JsonNode personalizacion = opcion.get("personalizacion");
            assertNotNull(personalizacion, "La personalización no debería ser nula.");
            assertNotNull(personalizacion.get("idExterno"), "El campo 'idExterno' de la personalización no debería ser nulo.");
            assertNotNull(personalizacion.get("nombre"), "El campo 'nombre' de la personalización no debería ser nulo.");
            assertFalse(personalizacion.get("nombre").asText().isEmpty(), "El campo 'nombre' de la personalización no debería estar vacío.");
        }
    }
    @Test
    public void testGetAdicionales_Success() throws IOException {
        // Mockear la respuesta del método getDataFromApi para devolver un JSON simple con adicionales
        String mockJsonResponse = "[" +
            "{\"id\": 1504, \"idExterno\": 1, \"nombre\": \"Mouse\", \"descripcion\": \"Mouse Bluetooth 3 teclas\", " +
            "\"dispositivo\": {\"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}," +
            "{\"id\": 1505, \"idExterno\": 2, \"nombre\": \"Teclado\", \"descripcion\": \"Teclado bluetooth\", " +
            "\"dispositivo\": {\"idExterno\": 1, \"nombre\": \"Lenovo IdeaPad 1 Laptop\"}}" +
            "]";

        // Simplificar la configuración de mock para evitar problemas de Mockito
        doReturn(mockJsonResponse).when(dataFetcherService).getDataFromApi();

        // Llamar al método y verificar que el resultado no sea nulo o vacío
        String result = dataFetcherService.getAdicionales();
        JsonNode resultNode = objectMapper.readTree(result);

        assertNotNull(resultNode, "El resultado no debería ser nulo.");
        assertFalse(resultNode.isEmpty(), "El resultado no debería estar vacío.");

        // Verificar que cada nodo contenga los campos básicos
        for (JsonNode adicional : resultNode) {
            assertNotNull(adicional.get("idExterno"), "El campo 'idExterno' no debería ser nulo.");
            assertNotNull(adicional.get("nombre"), "El campo 'nombre' no debería ser nulo.");
            assertNotNull(adicional.get("descripcion"), "El campo 'descripcion' no debería ser nulo.");

            // Verificar el dispositivo anidado
            JsonNode dispositivo = adicional.get("dispositivo");
            assertNotNull(dispositivo, "El dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("idExterno"), "El campo 'idExterno' del dispositivo no debería ser nulo.");
            assertNotNull(dispositivo.get("nombre"), "El campo 'nombre' del dispositivo no debería ser nulo.");
        }
    }
}
