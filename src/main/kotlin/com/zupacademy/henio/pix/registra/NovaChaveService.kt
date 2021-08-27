package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.ContasDeClientesNoItau
import com.zupacademy.henio.pix.exceptions.ChavePixExistenteException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class NovaChaveService(
    @Inject val repository: ChavePixRepository,
    @Inject val itauClient: ContasDeClientesNoItau
){

    @Transactional
    fun registra(@Valid novaChave: NovaChaveRequest): ChavePixEntity {

        if(repository.existsByChave(novaChave.valorChave))
            throw ChavePixExistenteException("Chave Pix ${novaChave.valorChave} existente")

        val response = itauClient.buscaContaPorTipo(novaChave.clienteId!!, novaChave.tipoConta!!.name)
        val conta = response.body()?.toModel() ?: throw IllegalStateException("Cliente não encontrado no Itau")

        val chave = novaChave.toModel(conta)
        repository.save(chave)

        return chave
    }

}
