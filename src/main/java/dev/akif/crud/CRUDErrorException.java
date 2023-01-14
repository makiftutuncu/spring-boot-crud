package dev.akif.crud;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class CRUDErrorException extends RuntimeException {
    CRUDError error;
}
