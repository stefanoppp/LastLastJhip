package um.prog2.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import um.prog2.domain.Venta;
import um.prog2.repository.VentaRepository;
import um.prog2.service.dto.VentaDTO;
import um.prog2.service.mapper.VentaMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class VentaServiceTest {

    @InjectMocks
    private VentaService ventaService;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaMapper ventaMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetVentaById_Success() {
        // Crea la venta y luego la mockea
        Long ventaId = 1L;
        Venta mockVenta = new Venta();
        mockVenta.setId(ventaId);

        // Mockear el comportamiento del repositorio para devolver la venta simulada
        when(ventaRepository.findOneWithEagerRelationships(anyLong())).thenReturn(Optional.of(mockVenta));

        // Mockear el comportamiento del mapper para convertir Venta a VentaDTO
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setId(ventaId);
        when(ventaMapper.toDto(mockVenta)).thenReturn(ventaDTO);

        // Llamar al método findOne del servicio para obtener la venta
        Optional<VentaDTO> result = ventaService.findOne(ventaId);

        // Verificar que el resultado no sea nulo y contenga la venta correcta
        assertNotNull(result, "El resultado no debería ser nulo.");
        assertNotNull(result.get(), "La venta debería existir.");
        assertNotNull(result.get().getId(), "El ID de la venta no debería ser nulo.");
    }
}
