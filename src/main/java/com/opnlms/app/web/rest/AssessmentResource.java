package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Assessment;
import com.opnlms.app.repository.AssessmentRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Assessment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AssessmentResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentResource.class);

    private static final String ENTITY_NAME = "assessment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssessmentRepository assessmentRepository;

    public AssessmentResource(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    /**
     * {@code POST  /assessments} : Create a new assessment.
     *
     * @param assessment the assessment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assessment, or with status {@code 400 (Bad Request)} if the assessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assessments")
    public Mono<ResponseEntity<Assessment>> createAssessment(@Valid @RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getId() != null) {
            throw new BadRequestAlertException("A new assessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return assessmentRepository
            .save(assessment)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/assessments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /assessments/:id} : Updates an existing assessment.
     *
     * @param id the id of the assessment to save.
     * @param assessment the assessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessment,
     * or with status {@code 400 (Bad Request)} if the assessment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assessments/{id}")
    public Mono<ResponseEntity<Assessment>> updateAssessment(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Assessment assessment
    ) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}, {}", id, assessment);
        if (assessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assessmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return assessmentRepository
                    .save(assessment.setIsPersisted())
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
     * {@code PATCH  /assessments/:id} : Partial updates given fields of an existing assessment, field will ignore if it is null
     *
     * @param id the id of the assessment to save.
     * @param assessment the assessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessment,
     * or with status {@code 400 (Bad Request)} if the assessment is not valid,
     * or with status {@code 404 (Not Found)} if the assessment is not found,
     * or with status {@code 500 (Internal Server Error)} if the assessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assessments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Assessment>> partialUpdateAssessment(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Assessment assessment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assessment partially : {}, {}", id, assessment);
        if (assessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assessmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Assessment> result = assessmentRepository
                    .findById(assessment.getId())
                    .map(existingAssessment -> {
                        if (assessment.getTitle() != null) {
                            existingAssessment.setTitle(assessment.getTitle());
                        }
                        if (assessment.getCourseId() != null) {
                            existingAssessment.setCourseId(assessment.getCourseId());
                        }
                        if (assessment.getSectionId() != null) {
                            existingAssessment.setSectionId(assessment.getSectionId());
                        }
                        if (assessment.getStudentId() != null) {
                            existingAssessment.setStudentId(assessment.getStudentId());
                        }
                        if (assessment.getExamDate() != null) {
                            existingAssessment.setExamDate(assessment.getExamDate());
                        }
                        if (assessment.getTimeLimit() != null) {
                            existingAssessment.setTimeLimit(assessment.getTimeLimit());
                        }
                        if (assessment.getScore() != null) {
                            existingAssessment.setScore(assessment.getScore());
                        }

                        return existingAssessment;
                    })
                    .flatMap(assessmentRepository::save);

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
     * {@code GET  /assessments} : get all the assessments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assessments in body.
     */
    @GetMapping("/assessments")
    public Mono<List<Assessment>> getAllAssessments() {
        log.debug("REST request to get all Assessments");
        return assessmentRepository.findAll().collectList();
    }

    /**
     * {@code GET  /assessments} : get all the assessments as a stream.
     * @return the {@link Flux} of assessments.
     */
    @GetMapping(value = "/assessments", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Assessment> getAllAssessmentsAsStream() {
        log.debug("REST request to get all Assessments as a stream");
        return assessmentRepository.findAll();
    }

    /**
     * {@code GET  /assessments/:id} : get the "id" assessment.
     *
     * @param id the id of the assessment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assessment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assessments/{id}")
    public Mono<ResponseEntity<Assessment>> getAssessment(@PathVariable String id) {
        log.debug("REST request to get Assessment : {}", id);
        Mono<Assessment> assessment = assessmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assessment);
    }

    /**
     * {@code DELETE  /assessments/:id} : delete the "id" assessment.
     *
     * @param id the id of the assessment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assessments/{id}")
    public Mono<ResponseEntity<Void>> deleteAssessment(@PathVariable String id) {
        log.debug("REST request to delete Assessment : {}", id);
        return assessmentRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
