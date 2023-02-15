package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Option;
import com.opnlms.app.repository.EntityManager;
import com.opnlms.app.repository.OptionRepository;
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
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OptionResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final String DEFAULT_ASSESSMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSESSMENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SELECTED = false;
    private static final Boolean UPDATED_IS_SELECTED = true;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Option option;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option()
            .text(DEFAULT_TEXT)
            .questionId(DEFAULT_QUESTION_ID)
            .correct(DEFAULT_CORRECT)
            .assessmentId(DEFAULT_ASSESSMENT_ID)
            .assignmentId(DEFAULT_ASSIGNMENT_ID)
            .isSelected(DEFAULT_IS_SELECTED);
        return option;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity(EntityManager em) {
        Option option = new Option()
            .text(UPDATED_TEXT)
            .questionId(UPDATED_QUESTION_ID)
            .correct(UPDATED_CORRECT)
            .assessmentId(UPDATED_ASSESSMENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .isSelected(UPDATED_IS_SELECTED);
        return option;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Option.class).block();
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
        option = createEntity(em);
    }

    @Test
    void createOption() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().collectList().block().size();
        // Create the Option
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate + 1);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testOption.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testOption.getCorrect()).isEqualTo(DEFAULT_CORRECT);
        assertThat(testOption.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
        assertThat(testOption.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testOption.getIsSelected()).isEqualTo(DEFAULT_IS_SELECTED);
    }

    @Test
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId("existing_id");

        int databaseSizeBeforeCreate = optionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().collectList().block().size();
        // set the field null
        option.setText(null);

        // Create the Option, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQuestionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().collectList().block().size();
        // set the field null
        option.setQuestionId(null);

        // Create the Option, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCorrectIsRequired() throws Exception {
        int databaseSizeBeforeTest = optionRepository.findAll().collectList().block().size();
        // set the field null
        option.setCorrect(null);

        // Create the Option, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOptionsAsStream() {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        List<Option> optionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Option.class)
            .getResponseBody()
            .filter(option::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(optionList).isNotNull();
        assertThat(optionList).hasSize(1);
        Option testOption = optionList.get(0);
        assertThat(testOption.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testOption.getQuestionId()).isEqualTo(DEFAULT_QUESTION_ID);
        assertThat(testOption.getCorrect()).isEqualTo(DEFAULT_CORRECT);
        assertThat(testOption.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
        assertThat(testOption.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testOption.getIsSelected()).isEqualTo(DEFAULT_IS_SELECTED);
    }

    @Test
    void getAllOptions() {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        // Get all the optionList
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
            .value(hasItem(option.getId()))
            .jsonPath("$.[*].text")
            .value(hasItem(DEFAULT_TEXT))
            .jsonPath("$.[*].questionId")
            .value(hasItem(DEFAULT_QUESTION_ID))
            .jsonPath("$.[*].correct")
            .value(hasItem(DEFAULT_CORRECT.booleanValue()))
            .jsonPath("$.[*].assessmentId")
            .value(hasItem(DEFAULT_ASSESSMENT_ID))
            .jsonPath("$.[*].assignmentId")
            .value(hasItem(DEFAULT_ASSIGNMENT_ID))
            .jsonPath("$.[*].isSelected")
            .value(hasItem(DEFAULT_IS_SELECTED.booleanValue()));
    }

    @Test
    void getOption() {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        // Get the option
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, option.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(option.getId()))
            .jsonPath("$.text")
            .value(is(DEFAULT_TEXT))
            .jsonPath("$.questionId")
            .value(is(DEFAULT_QUESTION_ID))
            .jsonPath("$.correct")
            .value(is(DEFAULT_CORRECT.booleanValue()))
            .jsonPath("$.assessmentId")
            .value(is(DEFAULT_ASSESSMENT_ID))
            .jsonPath("$.assignmentId")
            .value(is(DEFAULT_ASSIGNMENT_ID))
            .jsonPath("$.isSelected")
            .value(is(DEFAULT_IS_SELECTED.booleanValue()));
    }

    @Test
    void getNonExistingOption() {
        // Get the option
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOption() throws Exception {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).block();
        updatedOption
            .text(UPDATED_TEXT)
            .questionId(UPDATED_QUESTION_ID)
            .correct(UPDATED_CORRECT)
            .assessmentId(UPDATED_ASSESSMENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .isSelected(UPDATED_IS_SELECTED);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOption.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOption))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testOption.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testOption.getCorrect()).isEqualTo(UPDATED_CORRECT);
        assertThat(testOption.getAssessmentId()).isEqualTo(UPDATED_ASSESSMENT_ID);
        assertThat(testOption.getAssignmentId()).isEqualTo(UPDATED_ASSIGNMENT_ID);
        assertThat(testOption.getIsSelected()).isEqualTo(UPDATED_IS_SELECTED);
    }

    @Test
    void putNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, option.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.questionId(UPDATED_QUESTION_ID).correct(UPDATED_CORRECT).isSelected(UPDATED_IS_SELECTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOption.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testOption.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testOption.getCorrect()).isEqualTo(UPDATED_CORRECT);
        assertThat(testOption.getAssessmentId()).isEqualTo(DEFAULT_ASSESSMENT_ID);
        assertThat(testOption.getAssignmentId()).isEqualTo(DEFAULT_ASSIGNMENT_ID);
        assertThat(testOption.getIsSelected()).isEqualTo(UPDATED_IS_SELECTED);
    }

    @Test
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption
            .text(UPDATED_TEXT)
            .questionId(UPDATED_QUESTION_ID)
            .correct(UPDATED_CORRECT)
            .assessmentId(UPDATED_ASSESSMENT_ID)
            .assignmentId(UPDATED_ASSIGNMENT_ID)
            .isSelected(UPDATED_IS_SELECTED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOption.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testOption.getQuestionId()).isEqualTo(UPDATED_QUESTION_ID);
        assertThat(testOption.getCorrect()).isEqualTo(UPDATED_CORRECT);
        assertThat(testOption.getAssessmentId()).isEqualTo(UPDATED_ASSESSMENT_ID);
        assertThat(testOption.getAssignmentId()).isEqualTo(UPDATED_ASSIGNMENT_ID);
        assertThat(testOption.getIsSelected()).isEqualTo(UPDATED_IS_SELECTED);
    }

    @Test
    void patchNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, option.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().collectList().block().size();
        option.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(option))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOption() {
        // Initialize the database
        option.setId(UUID.randomUUID().toString());
        optionRepository.save(option).block();

        int databaseSizeBeforeDelete = optionRepository.findAll().collectList().block().size();

        // Delete the option
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, option.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Option> optionList = optionRepository.findAll().collectList().block();
        assertThat(optionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
