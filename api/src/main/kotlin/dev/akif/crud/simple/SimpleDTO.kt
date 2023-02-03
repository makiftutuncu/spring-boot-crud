package dev.akif.crud.simple

import dev.akif.crud.CRUDCreateDTO
import dev.akif.crud.CRUDDTO
import dev.akif.crud.CRUDUpdateDTO
import java.io.Serializable

/**
 * Simple version of [CRUDDTO] where create/update DTOs are just DTOs directly
 *
 * @param I Id type of the data
 */
interface SimpleDTO<out I : Serializable> : CRUDDTO<I>, CRUDCreateDTO, CRUDUpdateDTO
