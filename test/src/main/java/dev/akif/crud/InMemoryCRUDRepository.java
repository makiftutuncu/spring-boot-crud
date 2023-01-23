package dev.akif.crud;

import dev.akif.crud.error.CRUDErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * In-memory implementation of {@link CRUDRepository} that simulates an RDBMS for testing
 *
 * @param <I>      Id type of the data
 * @param <E>      Entity type of the data
 * @param <CM>     Create model type of the data
 * @param <Mapper> Mapper model type of the data
 */
public class InMemoryCRUDRepository<
        I extends Serializable,
        E extends CRUDEntity<I, E>,
        CM extends CRUDCreateModel<I, E>,
        Mapper extends CRUDTestMapper<I, E, ?, CM, ?>> implements CRUDRepository<I, E> {
    private final String type;
    private final Mapper mapper;
    private final BiPredicate<E, E> duplicateCheck;
    private final Map<I, E> entities;

    /**
     * Creates an in-memory repository containing given entities as initial data
     *
     * @param type            Type name of the data this repository manages
     * @param mapper          Mapper dependency of this repository
     * @param duplicate       Function to check whether two entities are duplicates
     * @param initialEntities Entities to set as initial data
     */
    @SafeVarargs
    public InMemoryCRUDRepository(final String type,
                                  final Mapper mapper,
                                  final BiPredicate<E, E> duplicate,
                                  final E... initialEntities
    ) {
        this.type = type;
        this.mapper = mapper;
        this.duplicateCheck = duplicate;
        this.entities = Arrays.stream(initialEntities).collect(Collectors.toMap(CRUDEntity::getId, e -> e));
    }

    @Override
    public Page<E> findAllByDeletedAtIsNull(final Pageable pageable) {
        return new PageImpl<>(
                entities
                        .values()
                        .stream()
                        .filter(e -> e.getDeletedAt() == null)
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                entities.size()
        );
    }

    @Override
    public Optional<E> findByIdAndDeletedAtIsNull(final I id) {
        return Optional.ofNullable(entities.get(id)).filter(e -> e.getDeletedAt() == null);
    }

    @Override
    public E save(final E entity) {
        handleDuplicates(entity);
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public int updateByVersion(final E entity, final int version) {
        handleDuplicates(entity);
        return Optional.ofNullable(entities.get(entity.getId()))
                .filter(e -> e.getVersion() == version)
                .map(e -> entities.replace(entity.getId(), entity))
                .map(e -> 1)
                .orElse(0);
    }

    @Override
    public void flush() {
    }

    /**
     * Clears the repository for testing
     */
    public void clear() {
        entities.clear();
    }

    private void handleDuplicates(final E entity) {
        if (entities.values().stream().anyMatch(e -> duplicateCheck.test(entity, e))) {
            throw CRUDErrorException.alreadyExists(type, mapper.entityToCreateModel(entity));
        }
    }
}
