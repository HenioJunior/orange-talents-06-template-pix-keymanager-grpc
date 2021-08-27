package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.ChaveEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface NovaChaveRepository: JpaRepository<ChaveEntity, UUID> {

    fun existsByChave(chave: String?): Boolean

}
