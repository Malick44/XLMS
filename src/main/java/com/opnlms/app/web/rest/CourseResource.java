package com.opnlms.app.web.rest;

import com.opnlms.app.domain.Course;
import com.opnlms.app.repository.CourseRepository;
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
 * REST controller for managing {@link com.opnlms.app.domain.Course}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseRepository courseRepository;

    public CourseResource(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public Mono<ResponseEntity<Course>> createCourse(@Valid @RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return courseRepository
            .save(course)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/courses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing course.
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses/{id}")
    public Mono<ResponseEntity<Course>> updateCourse(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to update Course : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return courseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return courseRepository
                    .save(course.setIsPersisted())
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
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing course, field will ignore if it is null
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 404 (Not Found)} if the course is not found,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Course>> partialUpdateCourse(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to partial update Course partially : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return courseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Course> result = courseRepository
                    .findById(course.getId())
                    .map(existingCourse -> {
                        if (course.getTitle() != null) {
                            existingCourse.setTitle(course.getTitle());
                        }
                        if (course.getAuthorId() != null) {
                            existingCourse.setAuthorId(course.getAuthorId());
                        }
                        if (course.getAuthorName() != null) {
                            existingCourse.setAuthorName(course.getAuthorName());
                        }
                        if (course.getDescription() != null) {
                            existingCourse.setDescription(course.getDescription());
                        }
                        if (course.getCategory() != null) {
                            existingCourse.setCategory(course.getCategory());
                        }
                        if (course.getSubCategory() != null) {
                            existingCourse.setSubCategory(course.getSubCategory());
                        }
                        if (course.getLevel() != null) {
                            existingCourse.setLevel(course.getLevel());
                        }
                        if (course.getLanguage() != null) {
                            existingCourse.setLanguage(course.getLanguage());
                        }
                        if (course.getDuration() != null) {
                            existingCourse.setDuration(course.getDuration());
                        }
                        if (course.getPrice() != null) {
                            existingCourse.setPrice(course.getPrice());
                        }
                        if (course.getRating() != null) {
                            existingCourse.setRating(course.getRating());
                        }
                        if (course.getRatingCount() != null) {
                            existingCourse.setRatingCount(course.getRatingCount());
                        }
                        if (course.getThumbnail() != null) {
                            existingCourse.setThumbnail(course.getThumbnail());
                        }
                        if (course.getUrl() != null) {
                            existingCourse.setUrl(course.getUrl());
                        }

                        return existingCourse;
                    })
                    .flatMap(courseRepository::save);

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
     * {@code GET  /courses} : get all the courses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public Mono<List<Course>> getAllCourses() {
        log.debug("REST request to get all Courses");
        return courseRepository.findAll().collectList();
    }

    /**
     * {@code GET  /courses} : get all the courses as a stream.
     * @return the {@link Flux} of courses.
     */
    @GetMapping(value = "/courses", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Course> getAllCoursesAsStream() {
        log.debug("REST request to get all Courses as a stream");
        return courseRepository.findAll();
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public Mono<ResponseEntity<Course>> getCourse(@PathVariable String id) {
        log.debug("REST request to get Course : {}", id);
        Mono<Course> course = courseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(course);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable String id) {
        log.debug("REST request to delete Course : {}", id);
        return courseRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
