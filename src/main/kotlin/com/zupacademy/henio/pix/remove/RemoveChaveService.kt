package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.BancoCentralClient
import com.zupacademy.henio.pix.cliente.bcb.DeletePixKeyRequest
import com.zupacademy.henio.pix.exceptions.ChavePixNaoEncontradaException
import com.zupacademy.henio.pix.exceptions.ClienteNaoEDonoDaChaveException
import com.zupacademy.henio.pix.exceptions.ErroDeComunicacaoComApiException
import com.zupacademy.henio.pix.exceptions.InconsistenciaDeDadosException
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
        clienteEDonoDaChave(chave, request.clienteId)
        deletaChaveNoBC(chave)

        repository.delete(chave)

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

    private fun clienteEDonoDaChave(chave: ChavePixEntity, clienteId: String?) {

        if(!chave.eDoCliente(clienteId!!)) {
            LOGGER.error("Solicitante não é o dono da chave")
            throw ClienteNaoEDonoDaChaveException("Solicitante não é o dono da chave")
        }
    }

    private fun deletaChaveNoBC(chave: ChavePixEntity) {

        val bcRequest = DeletePixKeyRequest(key = chave.chave, participant = chave.conta.ispb)
        val response = bcbClient.excluiChaveNoBC(chave.chave, bcRequest)

        if(response.status == HttpStatus.OK) {
            return
        }
        when(response.status) {
            HttpStatus.FORBIDDEN -> {
                LOGGER.error("Inconsistência de dados. Operação não permitida")
                throw InconsistenciaDeDadosException("Inconsistência de dados. Operação não permitida")
            }
            HttpStatus.NOT_FOUND -> {
                LOGGER.error("Inconsistência de dados. Chave não encontrada")
                throw InconsistenciaDeDadosException("Inconsistência de dados. Chave não encontrada")
            }
            else -> {
                LOGGER.error("Erro ao comunicar com o Banco Central")
                throw ErroDeComunicacaoComApiException("Erro ao comunicar com o Banco Central")
            }
        }
    }
}

