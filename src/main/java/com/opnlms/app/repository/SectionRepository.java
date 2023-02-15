package com.opnlms.app.repository;

import com.opnlms.app.domain.Section;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Section entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SectionRepository extends ReactiveCrudRepository<Section, String>, SectionRepositoryInternal {
    @Override
    <S extends Section> Mono<S> save(S entity);

    @Override
    Flux<Section> findAll();

    @Override
    Mono<Section> findById(String id);

    @Override
    Mono<Void> deleteById(String id);
}

interface SectionRepositoryInternal {
    <S extends Section> Mono<S> save(S entity);

    Flux<Section> findAllBy(Pageable pageable);

    Flux<Section> findAll();

    Mono<Section> findById(String id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Section> findAllBy(Pageable pageable, Criteria criteria);

}
