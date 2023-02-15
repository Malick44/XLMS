package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Question;
import com.opnlms.app.repository.QuestionRepository;
import com.opnlms.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.opnlms.app.domain.Question}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionRepository questionRepository;

    public QuestionResource(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param question the question to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new question, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    public Mono<ResponseEntity<Question>> createQuestion(@Valid @RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to save Question : {}", question);
        if (question.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return questionRepository
            .save(question)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/questions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the question to save.
     * @param question the question to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question,
     * or with status {@code 400 (Bad Request)} if the question is not valid,
     * or with status {@code 500 (Internal Server Error)} if the question couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions/{id}")
    public Mono<ResponseEntity<Question>> updateQuestion(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Question question
    ) throws URISyntaxException {
        log.debug("REST request to update Question : {}, {}", id, question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, question.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return questionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return questionRepository
                    .save(question.setIsPersisted())
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the question to save.
     * @param question the question to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question,
     * or with status {@code 400 (Bad Request)} if the question is not valid,
     * or with status {@code 404 (Not Found)} if the question is not found,
     * or with status {@code 500 (Internal Server Error)} if the question couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/questions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Question>> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Question question
    ) throws URISyntaxException {
        log.debug("REST request to partial update Question partially : {}, {}", id, question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, question.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return questionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Question> result = questionRepository
                    .findById(question.getId())
                    .map(existingQuestion -> {
                        if (question.getSectionId() != null) {
                            existingQuestion.setSectionId(question.getSectionId());
                        }
                        if (question.getCourseId() != null) {
                            existingQuestion.setCourseId(question.getCourseId());
                        }
                        if (question.getText() != null) {
                            existingQuestion.setText(question.getText());
                        }
                        if (question.getAssignmentId() != null) {
                            existingQuestion.setAssignmentId(question.getAssignmentId());
                        }
                        if (question.getAssessmentId() != null) {
                            existingQuestion.setAssessmentId(question.getAssessmentId());
                        }

                        return existingQuestion;
                    })
                    .flatMap(questionRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    public Mono<List<Question>> getAllQuestions() {
        log.debug("REST request to get all Questions");
        return questionRepository.findAll().collectList();
    }

    /**
     * {@code GET  /questions} : get all the questions as a stream.
     * @return the {@link Flux} of questions.
     */
    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Question> getAllQuestionsAsStream() {
        log.debug("REST request to get all Questions as a stream");
        return questionRepository.findAll();
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the question to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the question, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    public Mono<ResponseEntity<Question>> getQuestion(@PathVariable String id) {
        log.debug("REST request to get Question : {}", id);
        Mono<Question> question = questionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(question);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the question to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    public Mono<ResponseEntity<Void>> deleteQuestion(@PathVariable String id) {
        log.debug("REST request to delete Question : {}", id);
        return questionRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
