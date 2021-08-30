package com.zupacademy.henio.pix.cliente.bcb

import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.cliente.itau.ContaAssociada
import java.time.LocalDateTime

data class PixKeyDetailsResponse(
    val keyType: TipoDeChave,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {

    fun toChavePixInfo(): ChavePixInfo {

        return ChavePixInfo(
            tipoDeChave = keyType,
            chave = this.key,
            tipoDeConta = when (this.bankAccount.accountType) {
                BankAccount.AccountType.CACC -> TipoDeConta.CONTA_CORRENTE
                BankAccount.AccountType.SVGS -> TipoDeConta.CONTA_POUPANCA
            },
            conta = ContaAssociada(
                instituicao = bankAccount.participant,
                nomeDoTitular = owner.name,
                cpfDoTitular = owner.taxIdNumber,
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber
            ),
            criadaEm = createdAt,
            id = null,
            clienteId = null
        )
    }
}