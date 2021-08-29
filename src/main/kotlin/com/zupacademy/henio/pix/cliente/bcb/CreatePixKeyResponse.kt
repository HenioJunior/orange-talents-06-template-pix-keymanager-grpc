package com.zupacademy.henio.pix.cliente.bcb

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ContaAssociada
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.registra.NovaChaveRequest
import java.time.LocalDateTime
import java.util.*

data class CreatePixKeyResponse (
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {

    fun toChavePix(request: NovaChaveRequest, conta: ContaAssociada): ChavePixEntity {

        return ChavePixEntity(
            clienteId = UUID.fromString(request.clienteId),
            tipoChave = TipoDeChave.valueOf(keyType),
            chave = key,
            tipoConta = TipoDeConta.valueOf(request.tipoConta!!.name),
            conta = conta
        )
    }
}
