package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.ChaveEntity
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

    fun toModel(conta: ContaAssociada): ChaveEntity {
        return ChaveEntity(
            clienteId = UUID.fromString(this.clienteId),
            tipoChave = TipoDeChave.valueOf(this.tipoChave!!.name),
            chave = if(this.tipoChave == TipoDeChave.RANDOM) UUID.randomUUID().toString()
            else this.valorChave!!,
            tipoConta = TipoDeConta.valueOf(this.tipoConta!!.name),
            conta = conta
        )
    }
}
