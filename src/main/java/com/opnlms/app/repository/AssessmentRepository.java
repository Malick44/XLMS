package com.opnlms.app.repository;

import com.opnlms.app.domain.Assessment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Assessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssessmentRepository extends ReactiveCrudRepository<Assessment, String>, AssessmentRepositoryInternal {
    @Override
    <S extends Assessment> Mono<S> save(S entity);

    @Override
    Flux<Assessment> findAll();

    @Override
    Mono<Assessment> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface AssessmentRepositoryInternal {
    <S extends Assessment> Mono<S> save(S entity);

    Flux<Assessment> findAllBy(Pageable pageable);

    Flux<Assessment> findAll();

    Mono<Assessment> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Assessment> findAllBy(Pageable pageable, Criteria criteria);

}
