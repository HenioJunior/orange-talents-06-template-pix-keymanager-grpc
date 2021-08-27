package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.exceptions.ChavePixNaoEncontradaException
import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank


@Validated
@Singleton
class ExcluiChaveService(@Inject val repository: ChavePixRepository) {

    @Transactional
    fun exclui(
        @NotBlank @ValidaUUID(message = "cliente ID com formato inválido") clienteId: String?,
        @NotBlank @ValidaUUID(message = "pix ID com formato inválido") pixId: String?,
    ){

        val uuidPixId = UUID.fromString(pixId)
        val uuidClienteId = UUID.fromString(clienteId)

        val chave: Optional<ChavePixEntity> = repository.findByIdAndClienteId(uuidPixId, uuidClienteId)
        if(chave.isEmpty) {
            throw ChavePixNaoEncontradaException("Chave Pix não encontrada")
        }
        repository.deleteById(uuidPixId)
    }
}
