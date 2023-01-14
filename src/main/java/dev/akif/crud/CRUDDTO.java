package dev.akif.crud;

import java.io.Serializable;
import java.time.Instant;

public interface CRUDDTO<I extends Serializable> {
    I id();

    Instant createdAt();

    Instant updatedAt();
}
