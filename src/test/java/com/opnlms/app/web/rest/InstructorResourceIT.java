package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Instructor;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.InstructorRepository;
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
 * Integration tests for the {@link InstructorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InstructorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/instructors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Instructor instructor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createEntity(EntityManager em) {
        Instructor instructor = new Instructor().name(DEFAULT_NAME).email(DEFAULT_EMAIL);
        return instructor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instructor createUpdatedEntity(EntityManager em) {
        Instructor instructor = new Instructor().name(UPDATED_NAME).email(UPDATED_EMAIL);
        return instructor;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Instructor.class).block();
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
        instructor = createEntity(em);
    }

    @Test
    void createInstructor() throws Exception {
        int databaseSizeBeforeCreate = instructorRepository.findAll().collectList().block().size();
        // Create the Instructor
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate + 1);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createInstructorWithExistingId() throws Exception {
        // Create the Instructor with an existing ID
        instructor.setId("existing_id");

        int databaseSizeBeforeCreate = instructorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().collectList().block().size();
        // set the field null
        instructor.setName(null);

        // Create the Instructor, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = instructorRepository.findAll().collectList().block().size();
        // set the field null
        instructor.setEmail(null);

        // Create the Instructor, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInstructorsAsStream() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        List<Instructor> instructorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Instructor.class)
            .getResponseBody()
            .filter(instructor::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(instructorList).isNotNull();
        assertThat(instructorList).hasSize(1);
        Instructor testInstructor = instructorList.get(0);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void getAllInstructors() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        // Get all the instructorList
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
            .value(hasItem(instructor.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @Test
    void getInstructor() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        // Get the instructor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(instructor.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingInstructor() {
        // Get the instructor
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInstructor() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor
        Instructor updatedInstructor = instructorRepository.findById(instructor.getId()).block();
        updatedInstructor.name(UPDATED_NAME).email(UPDATED_EMAIL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedInstructor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor.email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void fullUpdateInstructorWithPatch() throws Exception {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();

        // Update the instructor using partial update
        Instructor partialUpdatedInstructor = new Instructor();
        partialUpdatedInstructor.setId(instructor.getId());

        partialUpdatedInstructor.name(UPDATED_NAME).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInstructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInstructor))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
        Instructor testInstructor = instructorList.get(instructorList.size() - 1);
        assertThat(testInstructor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInstructor.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInstructor() throws Exception {
        int databaseSizeBeforeUpdate = instructorRepository.findAll().collectList().block().size();
        instructor.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(instructor))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Instructor in the database
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInstructor() {
        // Initialize the database
        instructor.setId(UUID.randomUUID().toString());
        instructorRepository.save(instructor).block();

        int databaseSizeBeforeDelete = instructorRepository.findAll().collectList().block().size();

        // Delete the instructor
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, instructor.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Instructor> instructorList = instructorRepository.findAll().collectList().block();
        assertThat(instructorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
