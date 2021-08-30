package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePix
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.bcb.BancoCentralClient
import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyRequest
import com.zupacademy.henio.pix.cliente.itau.BancoItauClient
import com.zupacademy.henio.pix.exceptions.ChavePixExistenteException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChavePixService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: BancoItauClient,
    @Inject val bcbClient: BancoCentralClient
){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registra(@Valid novaChave: NovaChavePixRequest): ChavePix {

        if(repository.existsByChave(novaChave.valorDaChave))
            throw ChavePixExistenteException("Chave Pix ${novaChave.valorDaChave} existente")

        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoDeConta!!.name)
        val conta = response.body()?.associaConta() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        var chave = novaChave.toModel(conta)
        repository.save(chave)

        val bcbRequest = CreatePixKeyRequest.of(chave).also {
            LOGGER.info("Registrando chave Pix no Banco Central do Brasil (BCB): $it")
        }

        val bcbResponse = bcbClient.cadastraChaveNoBC(bcbRequest)
        if(bcbResponse.status != HttpStatus.CREATED)

            throw IllegalStateException("Erro ao registrar chave Pix no Banco Central do Brasil (BCB)")

//        chave = bcbResponse.body()!!.toChavePix(novaChave, conta)

        return chave
    }

}

