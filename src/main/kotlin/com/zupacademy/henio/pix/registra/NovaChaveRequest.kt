package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.chave.ContaAssociada
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

    fun toModel(conta: ContaAssociada): ChavePixEntity {
        return ChavePixEntity(
            clienteId = UUID.fromString(this.clienteId),
            tipoChave = TipoDeChave.valueOf(this.tipoChave!!.name),
            chave = when (this.tipoChave) {
                TipoDeChave.RANDOM -> UUID.randomUUID().toString()
                TipoDeChave.CPF -> conta.cpfDoTitular
                else -> this.valorChave.toString()
            },
            tipoConta = TipoDeConta.valueOf(this.tipoConta!!.name),
            conta = conta
        )
    }
}
