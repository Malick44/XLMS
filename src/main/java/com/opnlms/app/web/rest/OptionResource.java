package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Option;
import com.opnlms.app.repository.OptionRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Option}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);

    private static final String ENTITY_NAME = "option";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionRepository optionRepository;

    public OptionResource(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * {@code POST  /options} : Create a new option.
     *
     * @param option the option to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new option, or with status {@code 400 (Bad Request)} if the option has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/options")
    public Mono<ResponseEntity<Option>> createOption(@Valid @RequestBody Option option) throws URISyntaxException {
        log.debug("REST request to save Option : {}", option);
        if (option.getId() != null) {
            throw new BadRequestAlertException("A new option cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return optionRepository
            .save(option)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/options/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /options/:id} : Updates an existing option.
     *
     * @param id the id of the option to save.
     * @param option the option to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated option,
     * or with status {@code 400 (Bad Request)} if the option is not valid,
     * or with status {@code 500 (Internal Server Error)} if the option couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/options/{id}")
    public Mono<ResponseEntity<Option>> updateOption(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Option option
    ) throws URISyntaxException {
        log.debug("REST request to update Option : {}, {}", id, option);
        if (option.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, option.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return optionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return optionRepository
                    .save(option.setIsPersisted())
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
     * {@code PATCH  /options/:id} : Partial updates given fields of an existing option, field will ignore if it is null
     *
     * @param id the id of the option to save.
     * @param option the option to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated option,
     * or with status {@code 400 (Bad Request)} if the option is not valid,
     * or with status {@code 404 (Not Found)} if the option is not found,
     * or with status {@code 500 (Internal Server Error)} if the option couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/options/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Option>> partialUpdateOption(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Option option
    ) throws URISyntaxException {
        log.debug("REST request to partial update Option partially : {}, {}", id, option);
        if (option.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, option.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return optionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Option> result = optionRepository
                    .findById(option.getId())
                    .map(existingOption -> {
                        if (option.getText() != null) {
                            existingOption.setText(option.getText());
                        }
                        if (option.getQuestionId() != null) {
                            existingOption.setQuestionId(option.getQuestionId());
                        }
                        if (option.getCorrect() != null) {
                            existingOption.setCorrect(option.getCorrect());
                        }
                        if (option.getAssessmentId() != null) {
                            existingOption.setAssessmentId(option.getAssessmentId());
                        }
                        if (option.getAssignmentId() != null) {
                            existingOption.setAssignmentId(option.getAssignmentId());
                        }
                        if (option.getIsSelected() != null) {
                            existingOption.setIsSelected(option.getIsSelected());
                        }

                        return existingOption;
                    })
                    .flatMap(optionRepository::save);

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
     * {@code GET  /options} : get all the options.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of options in body.
     */
    @GetMapping("/options")
    public Mono<List<Option>> getAllOptions() {
        log.debug("REST request to get all Options");
        return optionRepository.findAll().collectList();
    }

    /**
     * {@code GET  /options} : get all the options as a stream.
     * @return the {@link Flux} of options.
     */
    @GetMapping(value = "/options", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Option> getAllOptionsAsStream() {
        log.debug("REST request to get all Options as a stream");
        return optionRepository.findAll();
    }

    /**
     * {@code GET  /options/:id} : get the "id" option.
     *
     * @param id the id of the option to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the option, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/options/{id}")
    public Mono<ResponseEntity<Option>> getOption(@PathVariable String id) {
        log.debug("REST request to get Option : {}", id);
        Mono<Option> option = optionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(option);
    }

    /**
     * {@code DELETE  /options/:id} : delete the "id" option.
     *
     * @param id the id of the option to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/options/{id}")
    public Mono<ResponseEntity<Void>> deleteOption(@PathVariable String id) {
        log.debug("REST request to delete Option : {}", id);
        return optionRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
