package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.ChavePixEntity
import com.zupacademy.henio.pix.TipoChave
import com.zupacademy.henio.pix.TipoDeChave
import com.zupacademy.henio.pix.TipoDeConta
import com.zupacademy.henio.pix.validacoes.ValidaChavePix
import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidaChavePix
@Introspected
class NovaChavePix(
    @ValidaUUID
    @field: NotBlank
    val clienteId: String?,

    @field: NotNull
    val tipo: TipoDeChave?,

    @field: Size(max = 77)
    val chave: String?,

    @field: NotNull
    val tipoDeConta: TipoDeConta?
) {

    fun toModel(conta: ContaAssociada): ChavePixEntity {
        return ChavePixEntity(
            clienteId = UUID.fromString(this.clienteId),
            tipo = TipoDeChave.valueOf(this.tipo!!.name),
            chave = if(this.tipo == TipoDeChave.RANDOM) UUID.randomUUID().toString()
            else this.chave!!,
            tipoDeConta = TipoDeConta.valueOf(this.tipoDeConta!!.name),
            conta = conta
        )
    }
}
