package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.BancoCentralClient
import com.zupacademy.henio.pix.cliente.DeletePixKeyRequest
import com.zupacademy.henio.pix.exceptions.ChavePixNaoEncontradaException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid


@Validated
@Singleton
class RemoveChaveService(
    @Inject val repository: ChavePixRepository,
    @Inject val bcbClient: BancoCentralClient
    ){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun exclui(@Valid request: RemoveChaveRequest): ChavePixEntity{

        val chave: ChavePixEntity = buscarChave(request)

        repository.delete(chave)

        val request = DeletePixKeyRequest(chave.chave)

        val bcbResponse = bcbClient.excluiChaveNoBC(key = chave.chave, request = request)
        if(bcbResponse.status != HttpStatus.OK) {
            throw IllegalStateException("Erro ao remover chave Pix no Banco Central do Brasil (BCB)")
        }

        return chave
    }


    private fun buscarChave(request: RemoveChaveRequest): ChavePixEntity {

        val possivelChave:Optional<ChavePixEntity> = repository.findById(UUID.fromString(request.pixId))
        if(possivelChave.isEmpty) {
            LOGGER.error("Chave não encontrada")
            throw ChavePixNaoEncontradaException("Chave não encontrada")
        }
        return possivelChave.get()
    }
}

