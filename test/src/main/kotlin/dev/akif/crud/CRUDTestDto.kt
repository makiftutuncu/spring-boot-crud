package dev.akif.crud

import java.io.Serializable

abstract class CRUDTestDto <I : Serializable, E : CRUDCreateDTO, D : CRUDDTO<I>> {

    abstract val testCreateDto: E

    abstract val testDto: D
}