package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.ChavePixEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePixEntity, UUID> {

    fun existsByChave(chave: String?): Boolean

}
