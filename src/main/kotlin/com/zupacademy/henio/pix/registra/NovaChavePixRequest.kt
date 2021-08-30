package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePix
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.cliente.itau.ContaAssociada
import com.zupacademy.henio.pix.validacoes.ValidaChavePix
import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidaChavePix
@Introspected
class NovaChavePixRequest(
    @ValidaUUID
    @field: NotBlank
    val clienteId: String?,

    @field: NotNull
    val tipoDeChave: TipoDeChave?,

    @field: Size(max = 77)
    val valorDaChave: String?,

    @field: NotNull
    val tipoDeConta: TipoDeConta?
) {

    fun toModel(conta: ContaAssociada): ChavePix {
        return ChavePix(
            clienteId = UUID.fromString(this.clienteId),
            tipoDeChave = TipoDeChave.valueOf(this.tipoDeChave!!.name),
            chave = when (this.tipoDeChave) {
                TipoDeChave.RANDOM -> UUID.randomUUID().toString()
                TipoDeChave.CPF -> conta.cpfDoTitular
                else -> this.valorDaChave.toString()
            },
            tipoDeConta = TipoDeConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }
}
