package com.opnlms.app.repository;

import com.opnlms.app.domain.Option;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Option entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionRepository extends ReactiveCrudRepository<Option, String>, OptionRepositoryInternal {
    @Override
    <S extends Option> Mono<S> save(S entity);

    @Override
    Flux<Option> findAll();

    @Override
    Mono<Option> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface OptionRepositoryInternal {
    <S extends Option> Mono<S> save(S entity);

    Flux<Option> findAllBy(Pageable pageable);

    Flux<Option> findAll();

    Mono<Option> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Option> findAllBy(Pageable pageable, Criteria criteria);

}
