package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePix
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.bcb.BancoCentralClient
import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyRequest
import com.zupacademy.henio.pix.cliente.itau.BancoItauClient
import com.zupacademy.henio.pix.exceptions.ChavePixExistenteException
import com.zupacademy.henio.pix.exceptions.ErroDeComunicacaoComApiException
import com.zupacademy.henio.pix.exceptions.InconsistenciaDeDadosException
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
    fun registra(@Valid novaChave: NovaChavePix): ChavePix {

        if(repository.existsByChave(novaChave.chave))
            throw ChavePixExistenteException("Chave Pix ${novaChave.chave} existente")

        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoDeConta!!.name)
        val conta = response.body()?.associaConta() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        val chave = novaChave.paraChavePix(conta)
        repository.save(chave)

        val bcbRequest = CreatePixKeyRequest.of(chave).also {
            LOGGER.info("Registrando chave Pix no Banco Central do Brasil (BCB: $it)")
        }
        val bcbResponse = bcbClient.registraChaveNoBC(bcbRequest)

        when(bcbResponse.status) {
            HttpStatus.CREATED -> return bcbResponse.body()!!.toBcbChavePix(novaChave, conta)
            HttpStatus.UNPROCESSABLE_ENTITY -> {
                LOGGER.error("Inconsistencia de dados. Chave já registrada no Banco Central")
                throw InconsistenciaDeDadosException(
                    "Inconsistencia de dados. Chave já registrada no Banco Central")
            }
            else -> {
                LOGGER.error("Erro ao comunicar com o Banco Central")
                throw ErroDeComunicacaoComApiException("Erro ao comunicar com o Banco Central")
            }
        }

        return chave
    }
}

