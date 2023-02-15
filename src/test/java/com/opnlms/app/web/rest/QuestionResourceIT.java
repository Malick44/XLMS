package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Question;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.QuestionRepository;
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
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_SECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSESSMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSESSMENT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .sectionId(DEFAULT_SECTION_ID)
            .courseId(DEFAULT_COURSE_ID)
            .text(DEFAULT_TEXT)
            .assignmentId(DEFAULT_ASSIGNMENT_ID)
            .assessmentId(DEFAULT_ASSESSMENT_ID);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .sectionId(UPDATED_SECTION_ID)
            .courseId(UPDATED_COURSE_ID)
            .text(UPDATED_TEXT)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .assessmentId(UPDATED_ASSESSMENT_ID);
        return question;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Question.class).block();
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
        question = createEntity(em);
    }

    @Test
    void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().collectList().block().size();
        // Create the Question
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testQuestion.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testQuestion.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testQuestion.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testQuestion.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
    }

    @Test
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        question.setId("existing_id");

        int databaseSizeBeforeCreate = questionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkSectionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().collectList().block().size();
        // set the field null
        question.setSectionId(null);

        // Create the Question, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllQuestionsAsStream() {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        List<Question> questionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Question.class)
            .getResponseBody()
            .filter(question::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(questionList).isNotNull();
        assertThat(questionList).hasSize(1);
        Question testQuestion = questionList.get(0);
        assertThat(testQuestion.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testQuestion.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testQuestion.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testQuestion.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testQuestion.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
    }

    @Test
    void getAllQuestions() {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        // Get all the questionList
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
            .value(hasItem(question.getId()))
            .jsonPath("$.[*].sectionId")
            .value(hasItem(DEFAULT_SECTION_ID))
            .jsonPath("$.[*].courseId")
            .value(hasItem(DEFAULT_COURSE_ID))
            .jsonPath("$.[*].text")
            .value(hasItem(DEFAULT_TEXT))
            .jsonPath("$.[*].assignmentId")
            .value(hasItem(DEFAULT_ASSIGNMENT_ID))
            .jsonPath("$.[*].assessmentId")
            .value(hasItem(DEFAULT_ASSESSMENT_ID));
    }

    @Test
    void getQuestion() {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(question.getId()))
            .jsonPath("$.sectionId")
            .value(is(DEFAULT_SECTION_ID))
            .jsonPath("$.courseId")
            .value(is(DEFAULT_COURSE_ID))
            .jsonPath("$.text")
            .value(is(DEFAULT_TEXT))
            .jsonPath("$.assignmentId")
            .value(is(DEFAULT_ASSIGNMENT_ID))
            .jsonPath("$.assessmentId")
            .value(is(DEFAULT_ASSESSMENT_ID));
    }

    @Test
    void getNonExistingQuestion() {
        // Get the question
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingQuestion() throws Exception {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).block();
        updatedQuestion
            .sectionId(UPDATED_SECTION_ID)
            .courseId(UPDATED_COURSE_ID)
            .text(UPDATED_TEXT)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .assessmentId(UPDATED_ASSESSMENT_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedQuestion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuestion.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getAssignmentId()).isEqualTo(UPDATED_ASSIGNMENT_ID);
        assertThat(testQuestion.getAssessmentId()).isEqualTo(UPDATED_ASSESSMENT_ID);
    }

    @Test
    void putNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, question.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.sectionId(UPDATED_SECTION_ID).courseId(UPDATED_COURSE_ID).text(UPDATED_TEXT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuestion.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testQuestion.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
    }

    @Test
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion
            .sectionId(UPDATED_SECTION_ID)
            .courseId(UPDATED_COURSE_ID)
            .text(UPDATED_TEXT)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .assessmentId(UPDATED_ASSESSMENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testQuestion.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getAssignmentId()).isEqualTo(UPDATED_ASSIGNMENT_ID);
        assertThat(testQuestion.getAssessmentId()).isEqualTo(UPDATED_ASSESSMENT_ID);
    }

    @Test
    void patchNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, question.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().collectList().block().size();
        question.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(question))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteQuestion() {
        // Initialize the database
        question.setId(UUID.randomUUID().toString());
        questionRepository.save(question).block();

        int databaseSizeBeforeDelete = questionRepository.findAll().collectList().block().size();

        // Delete the question
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, question.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll().collectList().block();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
