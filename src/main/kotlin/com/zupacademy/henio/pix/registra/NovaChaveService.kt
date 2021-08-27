package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.BancoCentralClient
import com.zupacademy.henio.pix.cliente.ContasDeClientesNoItau
import com.zupacademy.henio.pix.cliente.CreatePixKeyRequest
import com.zupacademy.henio.pix.exceptions.ChavePixExistenteException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChaveService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: ContasDeClientesNoItau,
    @Inject val bcbClient: BancoCentralClient
){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registra(@Valid novaChave: NovaChaveRequest): ChavePixEntity {

        if(repository.existsByChave(novaChave.valorChave))
            throw ChavePixExistenteException("Chave Pix ${novaChave.valorChave} existente")

        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoConta!!.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        val chave = novaChave.toModel(conta)
        repository.save(chave)

        val bcbRequest = CreatePixKeyRequest.of(chave).also {
            LOGGER.info("Registrando chave Pix no Banco Central do Brasil (BCB): $it")
        }

        val bcbResponse = bcbClient.cadastraChaveNoBC(bcbRequest)
        if(bcbResponse.status != HttpStatus.CREATED)
            throw IllegalStateException("Erro ao registrar chave Pix no Banco Central do Brasil (BCB)")

        //chave.atualiza(bcbResponse.body()!!.key)
        return chave
    }

}

