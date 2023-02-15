package com.opnlms.app.repository;

import com.opnlms.app.domain.Instructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Instructor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InstructorRepository extends ReactiveCrudRepository<Instructor, String>, InstructorRepositoryInternal {
    @Override
    <S extends Instructor> Mono<S> save(S entity);

    @Override
    Flux<Instructor> findAll();

    @Override
    Mono<Instructor> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface InstructorRepositoryInternal {
    <S extends Instructor> Mono<S> save(S entity);

    Flux<Instructor> findAllBy(Pageable pageable);

    Flux<Instructor> findAll();

    Mono<Instructor> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Instructor> findAllBy(Pageable pageable, Criteria criteria);

}
