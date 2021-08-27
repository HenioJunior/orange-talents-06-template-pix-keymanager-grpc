package com.zupacademy.henio.pix.chave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePixEntity, UUID> {

    fun existsByChave(chave: String?): Boolean

    fun findByIdAndClienteId(uuidPixId: UUID?, uuidClienteId: UUID?): Optional<ChavePixEntity>
}
