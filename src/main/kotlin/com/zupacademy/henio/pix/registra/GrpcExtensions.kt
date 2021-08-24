package com.zupacademy.henio.pix.registra
import com.zupacademy.henio.pix.TipoChave.*
import com.zupacademy.henio.pix.TipoConta.*
import com.zupacademy.henio.pix.RegistraChavePixRequest
import com.zupacademy.henio.pix.TipoDeChave
import com.zupacademy.henio.pix.TipoDeConta

fun RegistraChavePixRequest.toModel(): NovaChavePix {

    return NovaChavePix(
        clienteId = clienteId,
        tipo = when(tipoDeChave) {
            UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoDeChave.name)
        },
        chave = chave,
        tipoDeConta = when(tipoDeConta) {
            UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoDeConta.name)
        }
    )
}