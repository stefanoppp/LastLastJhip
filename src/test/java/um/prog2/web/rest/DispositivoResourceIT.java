package um.prog2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static um.prog2.domain.DispositivoAsserts.*;
import static um.prog2.web.rest.TestUtil.createUpdateProxyForBean;
import static um.prog2.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import um.prog2.IntegrationTest;
import um.prog2.domain.Dispositivo;
import um.prog2.domain.enumeration.Moneda;
import um.prog2.repository.DispositivoRepository;
import um.prog2.service.dto.DispositivoDTO;
import um.prog2.service.mapper.DispositivoMapper;

/**
 * Integration tests for the {@link DispositivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispositivoResourceIT {

    private static final Long DEFAULT_ID_EXTERNO = 1L;
    private static final Long UPDATED_ID_EXTERNO = 2L;

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_BASE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_BASE = new BigDecimal(1);

    private static final Moneda DEFAULT_MONEDA = Moneda.ARS;
    private static final Moneda UPDATED_MONEDA = Moneda.USD;

    private static final String ENTITY_API_URL = "/api/dispositivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private DispositivoMapper dispositivoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispositivoMockMvc;

    private Dispositivo dispositivo;

    private Dispositivo insertedDispositivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createEntity() {
        return new Dispositivo()
            .idExterno(DEFAULT_ID_EXTERNO)
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioBase(DEFAULT_PRECIO_BASE)
            .moneda(DEFAULT_MONEDA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createUpdatedEntity() {
        return new Dispositivo()
            .idExterno(UPDATED_ID_EXTERNO)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
    }

    @BeforeEach
    public void initTest() {
        dispositivo = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDispositivo != null) {
            dispositivoRepository.delete(insertedDispositivo);
            insertedDispositivo = null;
        }
    }

    @Test
    @Transactional
    void createDispositivo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);
        var returnedDispositivoDTO = om.readValue(
            restDispositivoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DispositivoDTO.class
        );

        // Validate the Dispositivo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDispositivo = dispositivoMapper.toEntity(returnedDispositivoDTO);
        assertDispositivoUpdatableFieldsEquals(returnedDispositivo, getPersistedDispositivo(returnedDispositivo));

        insertedDispositivo = returnedDispositivo;
    }

    @Test
    @Transactional
    void createDispositivoWithExistingId() throws Exception {
        // Create the Dispositivo with an existing ID
        dispositivo.setId(1L);
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdExternoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setIdExterno(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setCodigo(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setNombre(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setDescripcion(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioBaseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setPrecioBase(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonedaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dispositivo.setMoneda(null);

        // Create the Dispositivo, which fails.
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        restDispositivoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDispositivos() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispositivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].idExterno").value(hasItem(DEFAULT_ID_EXTERNO.intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioBase").value(hasItem(sameNumber(DEFAULT_PRECIO_BASE))))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())));
    }

    @Test
    @Transactional
    void getDispositivo() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        // Get the dispositivo
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL_ID, dispositivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispositivo.getId().intValue()))
            .andExpect(jsonPath("$.idExterno").value(DEFAULT_ID_EXTERNO.intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioBase").value(sameNumber(DEFAULT_PRECIO_BASE)))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDispositivo() throws Exception {
        // Get the dispositivo
        restDispositivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDispositivo() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispositivo
        Dispositivo updatedDispositivo = dispositivoRepository.findById(dispositivo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDispositivo are not directly saved in db
        em.detach(updatedDispositivo);
        updatedDispositivo
            .idExterno(UPDATED_ID_EXTERNO)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(updatedDispositivo);

        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispositivoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDispositivoToMatchAllProperties(updatedDispositivo);
    }

    @Test
    @Transactional
    void putNonExistingDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo.idExterno(UPDATED_ID_EXTERNO).precioBase(UPDATED_PRECIO_BASE);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispositivoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDispositivo, dispositivo),
            getPersistedDispositivo(dispositivo)
        );
    }

    @Test
    @Transactional
    void fullUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo
            .idExterno(UPDATED_ID_EXTERNO)
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioBase(UPDATED_PRECIO_BASE)
            .moneda(UPDATED_MONEDA);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDispositivoUpdatableFieldsEquals(partialUpdatedDispositivo, getPersistedDispositivo(partialUpdatedDispositivo));
    }

    @Test
    @Transactional
    void patchNonExistingDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispositivoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dispositivoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispositivo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dispositivo.setId(longCount.incrementAndGet());

        // Create the Dispositivo
        DispositivoDTO dispositivoDTO = dispositivoMapper.toDto(dispositivo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dispositivoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispositivo() throws Exception {
        // Initialize the database
        insertedDispositivo = dispositivoRepository.saveAndFlush(dispositivo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dispositivo
        restDispositivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispositivo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dispositivoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Dispositivo getPersistedDispositivo(Dispositivo dispositivo) {
        return dispositivoRepository.findById(dispositivo.getId()).orElseThrow();
    }

    protected void assertPersistedDispositivoToMatchAllProperties(Dispositivo expectedDispositivo) {
        assertDispositivoAllPropertiesEquals(expectedDispositivo, getPersistedDispositivo(expectedDispositivo));
    }

    protected void assertPersistedDispositivoToMatchUpdatableProperties(Dispositivo expectedDispositivo) {
        assertDispositivoAllUpdatablePropertiesEquals(expectedDispositivo, getPersistedDispositivo(expectedDispositivo));
    }
}
