package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Instructor;
import com.opnlms.app.repository.InstructorRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Instructor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InstructorResource {

    private final Logger log = LoggerFactory.getLogger(InstructorResource.class);

    private static final String ENTITY_NAME = "instructor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstructorRepository instructorRepository;

    public InstructorResource(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /**
     * {@code POST  /instructors} : Create a new instructor.
     *
     * @param instructor the instructor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instructor, or with status {@code 400 (Bad Request)} if the instructor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instructors")
    public Mono<ResponseEntity<Instructor>> createInstructor(@Valid @RequestBody Instructor instructor) throws URISyntaxException {
        log.debug("REST request to save Instructor : {}", instructor);
        if (instructor.getId() != null) {
            throw new BadRequestAlertException("A new instructor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return instructorRepository
            .save(instructor)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/instructors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /instructors/:id} : Updates an existing instructor.
     *
     * @param id the id of the instructor to save.
     * @param instructor the instructor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instructor,
     * or with status {@code 400 (Bad Request)} if the instructor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instructor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instructors/{id}")
    public Mono<ResponseEntity<Instructor>> updateInstructor(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Instructor instructor
    ) throws URISyntaxException {
        log.debug("REST request to update Instructor : {}, {}", id, instructor);
        if (instructor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instructor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instructorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return instructorRepository
                    .save(instructor.setIsPersisted())
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
     * {@code PATCH  /instructors/:id} : Partial updates given fields of an existing instructor, field will ignore if it is null
     *
     * @param id the id of the instructor to save.
     * @param instructor the instructor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instructor,
     * or with status {@code 400 (Bad Request)} if the instructor is not valid,
     * or with status {@code 404 (Not Found)} if the instructor is not found,
     * or with status {@code 500 (Internal Server Error)} if the instructor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instructors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Instructor>> partialUpdateInstructor(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Instructor instructor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Instructor partially : {}, {}", id, instructor);
        if (instructor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instructor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return instructorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Instructor> result = instructorRepository
                    .findById(instructor.getId())
                    .map(existingInstructor -> {
                        if (instructor.getName() != null) {
                            existingInstructor.setName(instructor.getName());
                        }
                        if (instructor.getEmail() != null) {
                            existingInstructor.setEmail(instructor.getEmail());
                        }

                        return existingInstructor;
                    })
                    .flatMap(instructorRepository::save);

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
     * {@code GET  /instructors} : get all the instructors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instructors in body.
     */
    @GetMapping("/instructors")
    public Mono<List<Instructor>> getAllInstructors() {
        log.debug("REST request to get all Instructors");
        return instructorRepository.findAll().collectList();
    }

    /**
     * {@code GET  /instructors} : get all the instructors as a stream.
     * @return the {@link Flux} of instructors.
     */
    @GetMapping(value = "/instructors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Instructor> getAllInstructorsAsStream() {
        log.debug("REST request to get all Instructors as a stream");
        return instructorRepository.findAll();
    }

    /**
     * {@code GET  /instructors/:id} : get the "id" instructor.
     *
     * @param id the id of the instructor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instructor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instructors/{id}")
    public Mono<ResponseEntity<Instructor>> getInstructor(@PathVariable String id) {
        log.debug("REST request to get Instructor : {}", id);
        Mono<Instructor> instructor = instructorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(instructor);
    }

    /**
     * {@code DELETE  /instructors/:id} : delete the "id" instructor.
     *
     * @param id the id of the instructor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instructors/{id}")
    public Mono<ResponseEntity<Void>> deleteInstructor(@PathVariable String id) {
        log.debug("REST request to delete Instructor : {}", id);
        return instructorRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
