package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.bcb.BancoCentralClient
import com.zupacademy.henio.pix.cliente.bcb.DeletePixKeyRequest
import com.zupacademy.henio.pix.exceptions.ChavePixNaoEncontradaException
import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank


@Validated
@Singleton
class RemoveChavePixService(
    @Inject val repository: ChavePixRepository,
    @Inject val bcbClient: BancoCentralClient
    ) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun exclui(
        @NotBlank @ValidaUUID(message = "cliente ID com formato inválido") clienteId: String?,
        @NotBlank @ValidaUUID(message = "pix ID com formato inválido") pixId: String?
    ) {

        val uuidPixId = UUID.fromString(pixId)
        val uuidClientId = UUID.fromString(clienteId)

        val chave = repository.findByIdAndClienteId(uuidPixId, uuidClientId).orElseThrow {
            ChavePixNaoEncontradaException("Chave Pix não encontrada ou não pertence ao cliente")
        }

        repository.deleteById(uuidPixId)

        val request = DeletePixKeyRequest(chave.chave)

        val bcbResponse = bcbClient.excluiChaveNoBC(key = chave.chave, request = request)
        if(bcbResponse.status != HttpStatus.OK) {
            throw IllegalStateException("Erro ao remover chave Pix no Banco Central do Brasil (BCB)")
        }
    }
}

