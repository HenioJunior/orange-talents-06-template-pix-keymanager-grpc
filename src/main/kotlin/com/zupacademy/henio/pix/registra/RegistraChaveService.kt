package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.chave.ContaAssociada
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.cliente.BancoCentralClient
import com.zupacademy.henio.pix.cliente.ContasDeClientesNoItau
import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyRequest
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
class RegistraChaveService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: ContasDeClientesNoItau,
    @Inject val bcbClient: BancoCentralClient
){

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registra(@Valid novaChaveRequest: NovaChaveRequest): ChavePixEntity {

        val itauResponse = itauClient.buscaContaPorTipo(novaChaveRequest.clienteId!!, novaChaveRequest.tipoConta!!.name)
        val conta = itauResponse.body()?.associaConta() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        verificaSeChaveJaExiste(novaChaveRequest, conta)

        val chave : ChavePixEntity = cadastraChaveNoBC(novaChaveRequest, conta)

        repository.save(chave)

        return chave
    }

    private fun verificaSeChaveJaExiste(request: NovaChaveRequest, conta: ContaAssociada){

        val chaveJaExiste: Boolean = when(request.tipoChave) {
            TipoDeChave.CPF -> repository.existsByChave(conta.cpfDoTitular)
            TipoDeChave.RANDOM -> false
            else -> repository.existsByChave(request.valorChave)
        }
        if(chaveJaExiste) {
            LOGGER.error("Chave Pix já registrada")
            throw ChavePixExistenteException("Chave Pix já registrada")
        }
    }

    private fun cadastraChaveNoBC(novaChaveRequest: NovaChaveRequest, conta: ContaAssociada): ChavePixEntity {

        val bcRequest: CreatePixKeyRequest = novaChaveRequest.toBcRequest(conta)
        val response = bcbClient.cadastraChaveNoBC(bcRequest)
        when(response.status) {
            HttpStatus.CREATED -> return response.body()!!.toChavePix(novaChaveRequest, conta)
            HttpStatus.UNPROCESSABLE_ENTITY -> {
                LOGGER.error("A Chave Pix já está registrada no Banco Central")
                throw InconsistenciaDeDadosException(
                    "A Chave Pix já está registrada no Banco Central")
            }
            else -> {
                LOGGER.error("Erro ao comunicar com o Banco Central")
                throw ErroDeComunicacaoComApiException("Erro ao comunicar com o Banco Central")
            }
        }
    }
 }



