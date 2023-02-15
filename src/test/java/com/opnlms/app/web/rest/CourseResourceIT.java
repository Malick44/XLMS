package com.opnlms.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.opnlms.app.IntegrationTest;
import com.opnlms.app.domain.Course;
import com.opnlms.app.repository.CourseRepository;
import com.opnlms.app.repository.EntityManager;
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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CourseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE = "AAAAAAAAAA";
    private static final String UPDATED_PRICE = "BBBBBBBBBB";

    private static final String DEFAULT_RATING = "AAAAAAAAAA";
    private static final String UPDATED_RATING = "BBBBBBBBBB";

    private static final String DEFAULT_RATING_COUNT = "AAAAAAAAAA";
    private static final String UPDATED_RATING_COUNT = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .title(DEFAULT_TITLE)
            .authorId(DEFAULT_AUTHOR_ID)
            .authorName(DEFAULT_AUTHOR_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .subCategory(DEFAULT_SUB_CATEGORY)
            .level(DEFAULT_LEVEL)
            .language(DEFAULT_LANGUAGE)
            .duration(DEFAULT_DURATION)
            .price(DEFAULT_PRICE)
            .rating(DEFAULT_RATING)
            .ratingCount(DEFAULT_RATING_COUNT)
            .thumbnail(DEFAULT_THUMBNAIL)
            .url(DEFAULT_URL);
        return course;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .title(UPDATED_TITLE)
            .authorId(UPDATED_AUTHOR_ID)
            .authorName(UPDATED_AUTHOR_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .subCategory(UPDATED_SUB_CATEGORY)
            .level(UPDATED_LEVEL)
            .language(UPDATED_LANGUAGE)
            .duration(UPDATED_DURATION)
            .price(UPDATED_PRICE)
            .rating(UPDATED_RATING)
            .ratingCount(UPDATED_RATING_COUNT)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL);
        return course;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Course.class).block();
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
        course = createEntity(em);
    }

    @Test
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().collectList().block().size();
        // Create the Course
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getAuthorId()).isEqualTo(DEFAULT_AUTHOR_ID);
        assertThat(testCourse.getAuthorName()).isEqualTo(DEFAULT_AUTHOR_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCourse.getSubCategory()).isEqualTo(DEFAULT_SUB_CATEGORY);
        assertThat(testCourse.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourse.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testCourse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testCourse.getRatingCount()).isEqualTo(DEFAULT_RATING_COUNT);
        assertThat(testCourse.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testCourse.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        course.setId("existing_id");

        int databaseSizeBeforeCreate = courseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().collectList().block().size();
        // set the field null
        course.setTitle(null);

        // Create the Course, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAuthorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().collectList().block().size();
        // set the field null
        course.setAuthorId(null);

        // Create the Course, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCoursesAsStream() {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        List<Course> courseList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Course.class)
            .getResponseBody()
            .filter(course::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(courseList).isNotNull();
        assertThat(courseList).hasSize(1);
        Course testCourse = courseList.get(0);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getAuthorId()).isEqualTo(DEFAULT_AUTHOR_ID);
        assertThat(testCourse.getAuthorName()).isEqualTo(DEFAULT_AUTHOR_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testCourse.getSubCategory()).isEqualTo(DEFAULT_SUB_CATEGORY);
        assertThat(testCourse.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourse.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testCourse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testCourse.getRatingCount()).isEqualTo(DEFAULT_RATING_COUNT);
        assertThat(testCourse.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testCourse.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    void getAllCourses() {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        // Get all the courseList
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
            .value(hasItem(course.getId()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].authorId")
            .value(hasItem(DEFAULT_AUTHOR_ID))
            .jsonPath("$.[*].authorName")
            .value(hasItem(DEFAULT_AUTHOR_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].category")
            .value(hasItem(DEFAULT_CATEGORY))
            .jsonPath("$.[*].subCategory")
            .value(hasItem(DEFAULT_SUB_CATEGORY))
            .jsonPath("$.[*].level")
            .value(hasItem(DEFAULT_LEVEL))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING))
            .jsonPath("$.[*].ratingCount")
            .value(hasItem(DEFAULT_RATING_COUNT))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(DEFAULT_THUMBNAIL))
            .jsonPath("$.[*].url")
            .value(hasItem(DEFAULT_URL));
    }

    @Test
    void getCourse() {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        // Get the course
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, course.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(course.getId()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.authorId")
            .value(is(DEFAULT_AUTHOR_ID))
            .jsonPath("$.authorName")
            .value(is(DEFAULT_AUTHOR_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.category")
            .value(is(DEFAULT_CATEGORY))
            .jsonPath("$.subCategory")
            .value(is(DEFAULT_SUB_CATEGORY))
            .jsonPath("$.level")
            .value(is(DEFAULT_LEVEL))
            .jsonPath("$.language")
            .value(is(DEFAULT_LANGUAGE))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING))
            .jsonPath("$.ratingCount")
            .value(is(DEFAULT_RATING_COUNT))
            .jsonPath("$.thumbnail")
            .value(is(DEFAULT_THUMBNAIL))
            .jsonPath("$.url")
            .value(is(DEFAULT_URL));
    }

    @Test
    void getNonExistingCourse() {
        // Get the course
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCourse() throws Exception {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).block();
        updatedCourse
            .title(UPDATED_TITLE)
            .authorId(UPDATED_AUTHOR_ID)
            .authorName(UPDATED_AUTHOR_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .subCategory(UPDATED_SUB_CATEGORY)
            .level(UPDATED_LEVEL)
            .language(UPDATED_LANGUAGE)
            .duration(UPDATED_DURATION)
            .price(UPDATED_PRICE)
            .rating(UPDATED_RATING)
            .ratingCount(UPDATED_RATING_COUNT)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCourse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCourse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getAuthorId()).isEqualTo(UPDATED_AUTHOR_ID);
        assertThat(testCourse.getAuthorName()).isEqualTo(UPDATED_AUTHOR_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCourse.getSubCategory()).isEqualTo(UPDATED_SUB_CATEGORY);
        assertThat(testCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCourse.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testCourse.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
        assertThat(testCourse.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testCourse.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, course.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .authorName(UPDATED_AUTHOR_NAME)
            .category(UPDATED_CATEGORY)
            .subCategory(UPDATED_SUB_CATEGORY)
            .language(UPDATED_LANGUAGE)
            .rating(UPDATED_RATING)
            .ratingCount(UPDATED_RATING_COUNT)
            .url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getAuthorId()).isEqualTo(DEFAULT_AUTHOR_ID);
        assertThat(testCourse.getAuthorName()).isEqualTo(UPDATED_AUTHOR_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCourse.getSubCategory()).isEqualTo(UPDATED_SUB_CATEGORY);
        assertThat(testCourse.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testCourse.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCourse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testCourse.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
        assertThat(testCourse.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testCourse.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .title(UPDATED_TITLE)
            .authorId(UPDATED_AUTHOR_ID)
            .authorName(UPDATED_AUTHOR_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .subCategory(UPDATED_SUB_CATEGORY)
            .level(UPDATED_LEVEL)
            .language(UPDATED_LANGUAGE)
            .duration(UPDATED_DURATION)
            .price(UPDATED_PRICE)
            .rating(UPDATED_RATING)
            .ratingCount(UPDATED_RATING_COUNT)
            .thumbnail(UPDATED_THUMBNAIL)
            .url(UPDATED_URL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getAuthorId()).isEqualTo(UPDATED_AUTHOR_ID);
        assertThat(testCourse.getAuthorName()).isEqualTo(UPDATED_AUTHOR_NAME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testCourse.getSubCategory()).isEqualTo(UPDATED_SUB_CATEGORY);
        assertThat(testCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testCourse.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testCourse.getRatingCount()).isEqualTo(UPDATED_RATING_COUNT);
        assertThat(testCourse.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testCourse.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, course.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().collectList().block().size();
        course.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(course))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCourse() {
        // Initialize the database
        course.setId(UUID.randomUUID().toString());
        courseRepository.save(course).block();

        int databaseSizeBeforeDelete = courseRepository.findAll().collectList().block().size();

        // Delete the course
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, course.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll().collectList().block();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
