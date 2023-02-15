package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Assignment;
import com.opnlms.app.repository.AssignmentRepository;
import com.opnlms.app.repository.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AssignmentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXAM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXAM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_TIME_LIMIT = 1;
    private static final Integer UPDATED_TIME_LIMIT = 2;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String ENTITY_API_URL = "/api/assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Assignment assignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createEntity(EntityManager em) {
        Assignment assignment = new Assignment()
            .title(DEFAULT_TITLE)
            .courseId(DEFAULT_COURSE_ID)
            .studentId(DEFAULT_STUDENT_ID)
            .examDate(DEFAULT_EXAM_DATE)
            .timeLimit(DEFAULT_TIME_LIMIT)
            .score(DEFAULT_SCORE);
        return assignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createUpdatedEntity(EntityManager em) {
        Assignment assignment = new Assignment()
            .title(UPDATED_TITLE)
            .courseId(UPDATED_COURSE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .examDate(UPDATED_EXAM_DATE)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);
        return assignment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Assignment.class).block();
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
        assignment = createEntity(em);
    }

    @Test
    void createAssignment() throws Exception {
        int databaseSizeBeforeCreate = assignmentRepository.findAll().collectList().block().size();
        // Create the Assignment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate + 1);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAssignment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testAssignment.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testAssignment.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testAssignment.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testAssignment.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void createAssignmentWithExistingId() throws Exception {
        // Create the Assignment with an existing ID
        assignment.setId("existing_id");

        int databaseSizeBeforeCreate = assignmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().collectList().block().size();
        // set the field null
        assignment.setCourseId(null);

        // Create the Assignment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().collectList().block().size();
        // set the field null
        assignment.setStudentId(null);

        // Create the Assignment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExamDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentRepository.findAll().collectList().block().size();
        // set the field null
        assignment.setExamDate(null);

        // Create the Assignment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAssignmentsAsStream() {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        List<Assignment> assignmentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Assignment.class)
            .getResponseBody()
            .filter(assignment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(assignmentList).isNotNull();
        assertThat(assignmentList).hasSize(1);
        Assignment testAssignment = assignmentList.get(0);
        assertThat(testAssignment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAssignment.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testAssignment.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testAssignment.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testAssignment.getTimeLimit()).isEqualTo(DEFAULT_TIME_LIMIT);
        assertThat(testAssignment.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    void getAllAssignments() {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        // Get all the assignmentList
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
            .value(hasItem(assignment.getId()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].studentId")
            .value(hasItem(DEFAULT_STUDENT_ID))
            .jsonPath("$.[*].examDate")
            .value(hasItem(DEFAULT_EXAM_DATE.toString()))
            .jsonPath("$.[*].timeLimit")
            .value(hasItem(DEFAULT_TIME_LIMIT))
            .jsonPath("$.[*].score")
            .value(hasItem(DEFAULT_SCORE));
    }

    @Test
    void getAssignment() {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        // Get the assignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, assignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(assignment.getId()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.studentId")
            .value(is(DEFAULT_STUDENT_ID))
            .jsonPath("$.examDate")
            .value(is(DEFAULT_EXAM_DATE.toString()))
            .jsonPath("$.timeLimit")
            .value(is(DEFAULT_TIME_LIMIT))
            .jsonPath("$.score")
            .value(is(DEFAULT_SCORE));
    }

    @Test
    void getNonExistingAssignment() {
        // Get the assignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAssignment() throws Exception {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();

        // Update the assignment
        Assignment updatedAssignment = assignmentRepository.findById(assignment.getId()).block();
        updatedAssignment
            .title(UPDATED_TITLE)
            .courseId(UPDATED_COURSE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .examDate(UPDATED_EXAM_DATE)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAssignment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssignment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssignment.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testAssignment.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testAssignment.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testAssignment.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void putNonExistingAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, assignment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment.title(UPDATED_TITLE).courseId(UPDATED_COURSE_ID).timeLimit(UPDATED_TIME_LIMIT).score(UPDATED_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssignment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssignment.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testAssignment.getExamDate()).isEqualTo(DEFAULT_EXAM_DATE);
        assertThat(testAssignment.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testAssignment.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void fullUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment
            .title(UPDATED_TITLE)
            .courseId(UPDATED_COURSE_ID)
            .studentId(UPDATED_STUDENT_ID)
            .examDate(UPDATED_EXAM_DATE)
            .timeLimit(UPDATED_TIME_LIMIT)
            .score(UPDATED_SCORE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
        Assignment testAssignment = assignmentList.get(assignmentList.size() - 1);
        assertThat(testAssignment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAssignment.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testAssignment.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testAssignment.getExamDate()).isEqualTo(UPDATED_EXAM_DATE);
        assertThat(testAssignment.getTimeLimit()).isEqualTo(UPDATED_TIME_LIMIT);
        assertThat(testAssignment.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    void patchNonExistingAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, assignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAssignment() throws Exception {
        int databaseSizeBeforeUpdate = assignmentRepository.findAll().collectList().block().size();
        assignment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(assignment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Assignment in the database
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAssignment() {
        // Initialize the database
        assignment.setId(UUID.randomUUID().toString());
        assignmentRepository.save(assignment).block();

        int databaseSizeBeforeDelete = assignmentRepository.findAll().collectList().block().size();

        // Delete the assignment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, assignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Assignment> assignmentList = assignmentRepository.findAll().collectList().block();
        assertThat(assignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
