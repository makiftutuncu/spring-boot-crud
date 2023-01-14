package dev.akif.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Optional;

public interface CRUDRepository<
        I extends Serializable,
        M extends CRUDModel<I>,
        E extends CRUDEntity<I, M>> {
    Page<E> findAllByDeleted(final Pageable pageable, final boolean deleted);

    Optional<E> findByIdAndDeleted(final I id, final boolean deleted);

    E save(final E entity);

    void flush();
}
