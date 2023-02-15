package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Section;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.SectionRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SectionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Section section;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .courseId(DEFAULT_COURSE_ID)
            .servicePath(DEFAULT_SERVICE_PATH);
        return section;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createUpdatedEntity(EntityManager em) {
        Section section = new Section()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .courseId(UPDATED_COURSE_ID)
            .servicePath(UPDATED_SERVICE_PATH);
        return section;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Section.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        section = createEntity(em);
    }

    @Test
    void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().collectList().block().size();
        // Create the Section
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSection.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testSection.getServicePath()).isEqualTo(DEFAULT_SERVICE_PATH);
    }

    @Test
    void createSectionWithExistingId() throws Exception {
        // Create the Section with an existing ID
        section.setId("existing_id");

        int databaseSizeBeforeCreate = sectionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSectionsAsStream() {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        List<Section> sectionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Section.class)
            .getResponseBody()
            .filter(section::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sectionList).isNotNull();
        assertThat(sectionList).hasSize(1);
        Section testSection = sectionList.get(0);
        assertThat(testSection.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSection.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testSection.getServicePath()).isEqualTo(DEFAULT_SERVICE_PATH);
    }

    @Test
    void getAllSections() {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        // Get all the sectionList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(section.getId()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].servicePath")
            .value(hasItem(DEFAULT_SERVICE_PATH));
    }

    @Test
    void getSection() {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        // Get the section
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, section.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(section.getId()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.servicePath")
            .value(is(DEFAULT_SERVICE_PATH));
    }

    @Test
    void getNonExistingSection() {
        // Get the section
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSection() throws Exception {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();

        // Update the section
        Section updatedSection = sectionRepository.findById(section.getId()).block();
        updatedSection.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).courseId(UPDATED_COURSE_ID).servicePath(UPDATED_SERVICE_PATH);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSection.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSection))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testSection.getServicePath()).isEqualTo(UPDATED_SERVICE_PATH);
    }

    @Test
    void putNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, section.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSection.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testSection.getServicePath()).isEqualTo(DEFAULT_SERVICE_PATH);
    }

    @Test
    void fullUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .courseId(UPDATED_COURSE_ID)
            .servicePath(UPDATED_SERVICE_PATH);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSection.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testSection.getServicePath()).isEqualTo(UPDATED_SERVICE_PATH);
    }

    @Test
    void patchNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, section.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().collectList().block().size();
        section.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(section))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSection() {
        // Initialize the database
        section.setId(UUID.randomUUID().toString());
        sectionRepository.save(section).block();

        int databaseSizeBeforeDelete = sectionRepository.findAll().collectList().block().size();

        // Delete the section
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, section.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Section> sectionList = sectionRepository.findAll().collectList().block();
        assertThat(sectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
