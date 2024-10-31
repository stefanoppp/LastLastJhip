package um.prog2.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static um.prog2.domain.AdicionalAsserts.*;
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
import um.prog2.domain.Adicional;
import um.prog2.domain.Dispositivo;
import um.prog2.repository.AdicionalRepository;
import um.prog2.service.dto.AdicionalDTO;
import um.prog2.service.mapper.AdicionalMapper;

/**
 * Integration tests for the {@link AdicionalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdicionalResourceIT {

    private static final Long DEFAULT_ID_EXTERNO = 1L;
    private static final Long UPDATED_ID_EXTERNO = 2L;

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRECIO_GRATIS = new BigDecimal(-1);
    private static final BigDecimal UPDATED_PRECIO_GRATIS = new BigDecimal(0);

    private static final String ENTITY_API_URL = "/api/adicionals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdicionalRepository adicionalRepository;

    @Autowired
    private AdicionalMapper adicionalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdicionalMockMvc;

    private Adicional adicional;

    private Adicional insertedAdicional;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createEntity(EntityManager em) {
        Adicional adicional = new Adicional()
            .idExterno(DEFAULT_ID_EXTERNO)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precio(DEFAULT_PRECIO)
            .precioGratis(DEFAULT_PRECIO_GRATIS);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createEntity();
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        adicional.setDispositivo(dispositivo);
        return adicional;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adicional createUpdatedEntity(EntityManager em) {
        Adicional updatedAdicional = new Adicional()
            .idExterno(UPDATED_ID_EXTERNO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        // Add required entity
        Dispositivo dispositivo;
        if (TestUtil.findAll(em, Dispositivo.class).isEmpty()) {
            dispositivo = DispositivoResourceIT.createUpdatedEntity();
            em.persist(dispositivo);
            em.flush();
        } else {
            dispositivo = TestUtil.findAll(em, Dispositivo.class).get(0);
        }
        updatedAdicional.setDispositivo(dispositivo);
        return updatedAdicional;
    }

    @BeforeEach
    public void initTest() {
        adicional = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdicional != null) {
            adicionalRepository.delete(insertedAdicional);
            insertedAdicional = null;
        }
    }

    @Test
    @Transactional
    void createAdicional() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);
        var returnedAdicionalDTO = om.readValue(
            restAdicionalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdicionalDTO.class
        );

        // Validate the Adicional in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdicional = adicionalMapper.toEntity(returnedAdicionalDTO);
        assertAdicionalUpdatableFieldsEquals(returnedAdicional, getPersistedAdicional(returnedAdicional));

        insertedAdicional = returnedAdicional;
    }

    @Test
    @Transactional
    void createAdicionalWithExistingId() throws Exception {
        // Create the Adicional with an existing ID
        adicional.setId(1L);
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdExternoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adicional.setIdExterno(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adicional.setNombre(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adicional.setDescripcion(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adicional.setPrecio(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioGratisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        adicional.setPrecioGratis(null);

        // Create the Adicional, which fails.
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        restAdicionalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdicionals() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        // Get all the adicionalList
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adicional.getId().intValue())))
            .andExpect(jsonPath("$.[*].idExterno").value(hasItem(DEFAULT_ID_EXTERNO.intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))))
            .andExpect(jsonPath("$.[*].precioGratis").value(hasItem(sameNumber(DEFAULT_PRECIO_GRATIS))));
    }

    @Test
    @Transactional
    void getAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        // Get the adicional
        restAdicionalMockMvc
            .perform(get(ENTITY_API_URL_ID, adicional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adicional.getId().intValue()))
            .andExpect(jsonPath("$.idExterno").value(DEFAULT_ID_EXTERNO.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)))
            .andExpect(jsonPath("$.precioGratis").value(sameNumber(DEFAULT_PRECIO_GRATIS)));
    }

    @Test
    @Transactional
    void getNonExistingAdicional() throws Exception {
        // Get the adicional
        restAdicionalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional
        Adicional updatedAdicional = adicionalRepository.findById(adicional.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdicional are not directly saved in db
        em.detach(updatedAdicional);
        updatedAdicional
            .idExterno(UPDATED_ID_EXTERNO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(updatedAdicional);

        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adicionalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdicionalToMatchAllProperties(updatedAdicional);
    }

    @Test
    @Transactional
    void putNonExistingAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional.descripcion(UPDATED_DESCRIPCION).precio(UPDATED_PRECIO).precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdicionalUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdicional, adicional),
            getPersistedAdicional(adicional)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdicionalWithPatch() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the adicional using partial update
        Adicional partialUpdatedAdicional = new Adicional();
        partialUpdatedAdicional.setId(adicional.getId());

        partialUpdatedAdicional
            .idExterno(UPDATED_ID_EXTERNO)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precio(UPDATED_PRECIO)
            .precioGratis(UPDATED_PRECIO_GRATIS);

        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdicional.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdicional))
            )
            .andExpect(status().isOk());

        // Validate the Adicional in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdicionalUpdatableFieldsEquals(partialUpdatedAdicional, getPersistedAdicional(partialUpdatedAdicional));
    }

    @Test
    @Transactional
    void patchNonExistingAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adicionalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(adicionalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdicional() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        adicional.setId(longCount.incrementAndGet());

        // Create the Adicional
        AdicionalDTO adicionalDTO = adicionalMapper.toDto(adicional);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdicionalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(adicionalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adicional in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdicional() throws Exception {
        // Initialize the database
        insertedAdicional = adicionalRepository.saveAndFlush(adicional);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the adicional
        restAdicionalMockMvc
            .perform(delete(ENTITY_API_URL_ID, adicional.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return adicionalRepository.count();
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

    protected Adicional getPersistedAdicional(Adicional adicional) {
        return adicionalRepository.findById(adicional.getId()).orElseThrow();
    }

    protected void assertPersistedAdicionalToMatchAllProperties(Adicional expectedAdicional) {
        assertAdicionalAllPropertiesEquals(expectedAdicional, getPersistedAdicional(expectedAdicional));
    }

    protected void assertPersistedAdicionalToMatchUpdatableProperties(Adicional expectedAdicional) {
        assertAdicionalAllUpdatablePropertiesEquals(expectedAdicional, getPersistedAdicional(expectedAdicional));
    }
}
