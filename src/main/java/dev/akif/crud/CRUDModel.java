package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

public interface CRUDModel<I extends Serializable> {
    I id();

    int version();

    Instant createdAt();

    Instant updatedAt();
}
