package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ContaAssociada
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.cliente.bcb.BankAccount
import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyRequest
import com.zupacademy.henio.pix.cliente.bcb.Owner
import com.zupacademy.henio.pix.validacoes.ValidaChavePix
import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidaChavePix
@Introspected
class NovaChaveRequest(
    @ValidaUUID
    @field: NotBlank
    val clienteId: String?,

    @field: NotNull
    val tipoChave: TipoDeChave?,

    @field: Size(max = 77)
    val valorChave: String?,

    @field: NotNull
    val tipoConta: TipoDeConta?
) {

    fun toBcRequest(conta: ContaAssociada): CreatePixKeyRequest {

        return CreatePixKeyRequest(
            keyType = tipoChave.toString(),
            key = when(tipoChave) {
                TipoDeChave.RANDOM -> ""
                TipoDeChave.CPF -> conta.cpfDoTitular
                else -> valorChave
            }!!,
            bankAccount = BankAccount(
                participant = conta.ispb,
                branch = conta.agencia,
                accountNumber = conta.numero,
                accountType = when(tipoConta!!) {
                    TipoDeConta.CONTA_CORRENTE -> BankAccount.AccountType.CACC
                    TipoDeConta.CONTA_POUPANCA -> BankAccount.AccountType.SVGS
                }
            ),
            owner = Owner(
                type = "NATURAL_PERSON",
                name = conta.nomeDoTitular,
                taxIdNumber = conta.cpfDoTitular
            )
        )
    }
}
