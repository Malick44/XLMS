package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Assignment;
import com.opnlms.app.repository.AssignmentRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Assignment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AssignmentResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

    private static final String ENTITY_NAME = "assignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentRepository assignmentRepository;

    public AssignmentResource(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * {@code POST  /assignments} : Create a new assignment.
     *
     * @param assignment the assignment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignment, or with status {@code 400 (Bad Request)} if the assignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assignments")
    public Mono<ResponseEntity<Assignment>> createAssignment(@Valid @RequestBody Assignment assignment) throws URISyntaxException {
        log.debug("REST request to save Assignment : {}", assignment);
        if (assignment.getId() != null) {
            throw new BadRequestAlertException("A new assignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return assignmentRepository
            .save(assignment)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/assignments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /assignments/:id} : Updates an existing assignment.
     *
     * @param id the id of the assignment to save.
     * @param assignment the assignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignment,
     * or with status {@code 400 (Bad Request)} if the assignment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assignments/{id}")
    public Mono<ResponseEntity<Assignment>> updateAssignment(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Assignment assignment
    ) throws URISyntaxException {
        log.debug("REST request to update Assignment : {}, {}", id, assignment);
        if (assignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return assignmentRepository
                    .save(assignment.setIsPersisted())
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
     * {@code PATCH  /assignments/:id} : Partial updates given fields of an existing assignment, field will ignore if it is null
     *
     * @param id the id of the assignment to save.
     * @param assignment the assignment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignment,
     * or with status {@code 400 (Bad Request)} if the assignment is not valid,
     * or with status {@code 404 (Not Found)} if the assignment is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assignments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Assignment>> partialUpdateAssignment(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Assignment assignment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assignment partially : {}, {}", id, assignment);
        if (assignment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Assignment> result = assignmentRepository
                    .findById(assignment.getId())
                    .map(existingAssignment -> {
                        if (assignment.getTitle() != null) {
                            existingAssignment.setTitle(assignment.getTitle());
                        }
                        if (assignment.getCourseId() != null) {
                            existingAssignment.setCourseId(assignment.getCourseId());
                        }
                        if (assignment.getStudentId() != null) {
                            existingAssignment.setStudentId(assignment.getStudentId());
                        }
                        if (assignment.getExamDate() != null) {
                            existingAssignment.setExamDate(assignment.getExamDate());
                        }
                        if (assignment.getTimeLimit() != null) {
                            existingAssignment.setTimeLimit(assignment.getTimeLimit());
                        }
                        if (assignment.getScore() != null) {
                            existingAssignment.setScore(assignment.getScore());
                        }

                        return existingAssignment;
                    })
                    .flatMap(assignmentRepository::save);

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
     * {@code GET  /assignments} : get all the assignments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignments in body.
     */
    @GetMapping("/assignments")
    public Mono<List<Assignment>> getAllAssignments() {
        log.debug("REST request to get all Assignments");
        return assignmentRepository.findAll().collectList();
    }

    /**
     * {@code GET  /assignments} : get all the assignments as a stream.
     * @return the {@link Flux} of assignments.
     */
    @GetMapping(value = "/assignments", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Assignment> getAllAssignmentsAsStream() {
        log.debug("REST request to get all Assignments as a stream");
        return assignmentRepository.findAll();
    }

    /**
     * {@code GET  /assignments/:id} : get the "id" assignment.
     *
     * @param id the id of the assignment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assignments/{id}")
    public Mono<ResponseEntity<Assignment>> getAssignment(@PathVariable String id) {
        log.debug("REST request to get Assignment : {}", id);
        Mono<Assignment> assignment = assignmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assignment);
    }

    /**
     * {@code DELETE  /assignments/:id} : delete the "id" assignment.
     *
     * @param id the id of the assignment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assignments/{id}")
    public Mono<ResponseEntity<Void>> deleteAssignment(@PathVariable String id) {
        log.debug("REST request to delete Assignment : {}", id);
        return assignmentRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
