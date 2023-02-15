package com.opnlms.app.repository;

import com.opnlms.app.domain.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, String>, CourseRepositoryInternal {
    @Override
    <S extends Course> Mono<S> save(S entity);

    @Override
    Flux<Course> findAll();

    @Override
    Mono<Course> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface CourseRepositoryInternal {
    <S extends Course> Mono<S> save(S entity);

    Flux<Course> findAllBy(Pageable pageable);

    Flux<Course> findAll();

    Mono<Course> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Course> findAllBy(Pageable pageable, Criteria criteria);

}
