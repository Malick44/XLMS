package com.opnlms.app.repository;

import com.opnlms.app.domain.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentRepository extends ReactiveCrudRepository<Assignment, String>, AssignmentRepositoryInternal {
    @Override
    <S extends Assignment> Mono<S> save(S entity);

    @Override
    Flux<Assignment> findAll();

    @Override
    Mono<Assignment> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface AssignmentRepositoryInternal {
    <S extends Assignment> Mono<S> save(S entity);

    Flux<Assignment> findAllBy(Pageable pageable);

    Flux<Assignment> findAll();

    Mono<Assignment> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Assignment> findAllBy(Pageable pageable, Criteria criteria);

}
