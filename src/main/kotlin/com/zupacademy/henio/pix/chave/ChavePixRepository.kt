package com.zupacademy.henio.pix.chave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, UUID> {

    fun existsByChave(chave: String?): Boolean

    fun findByIdAndClienteId(uuidPixId: UUID?, uuidClienteId: UUID?): Optional<ChavePix>

    fun findByChave(chave: String): Optional<ChavePix>

    fun findAllByClienteId(idCliente: UUID): MutableList<ChavePix>
}
