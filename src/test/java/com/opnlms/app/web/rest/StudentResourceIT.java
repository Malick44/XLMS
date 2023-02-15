package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Student;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.StudentRepository;
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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StudentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/students";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student().name(DEFAULT_NAME).email(DEFAULT_EMAIL);
        return student;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student().name(UPDATED_NAME).email(UPDATED_EMAIL);
        return student;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Student.class).block();
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
        student = createEntity(em);
    }

    @Test
    void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().collectList().block().size();
        // Create the Student
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void createStudentWithExistingId() throws Exception {
        // Create the Student with an existing ID
        student.setId("existing_id");

        int databaseSizeBeforeCreate = studentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().collectList().block().size();
        // set the field null
        student.setName(null);

        // Create the Student, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().collectList().block().size();
        // set the field null
        student.setEmail(null);

        // Create the Student, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllStudentsAsStream() {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        List<Student> studentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Student.class)
            .getResponseBody()
            .filter(student::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(studentList).isNotNull();
        assertThat(studentList).hasSize(1);
        Student testStudent = studentList.get(0);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void getAllStudents() {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        // Get all the studentList
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
            .value(hasItem(student.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL));
    }

    @Test
    void getStudent() {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        // Get the student
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, student.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(student.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL));
    }

    @Test
    void getNonExistingStudent() {
        // Get the student
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingStudent() throws Exception {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).block();
        updatedStudent.name(UPDATED_NAME).email(UPDATED_EMAIL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedStudent.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedStudent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void putNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, student.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    void fullUpdateStudentWithPatch() throws Exception {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();

        // Update the student using partial update
        Student partialUpdatedStudent = new Student();
        partialUpdatedStudent.setId(student.getId());

        partialUpdatedStudent.name(UPDATED_NAME).email(UPDATED_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStudent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStudent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void patchNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, student.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().collectList().block().size();
        student.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(student))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStudent() {
        // Initialize the database
        student.setId(UUID.randomUUID().toString());
        studentRepository.save(student).block();

        int databaseSizeBeforeDelete = studentRepository.findAll().collectList().block().size();

        // Delete the student
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, student.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll().collectList().block();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
