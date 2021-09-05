package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.grpc.RegistraChavePixRequest
import com.zupacademy.henio.pix.grpc.TipoChave
import com.zupacademy.henio.pix.grpc.TipoConta

fun RegistraChavePixRequest.toDto(): NovaChavePix {

    return NovaChavePix(
        clienteId = clienteId,
        tipoDeChave = when(tipoChave) {
            TipoChave.UNKNOWN_TIPO_CHAVE -> throw IllegalArgumentException("Tipo de Chave desconhecida")
            else -> TipoDeChave.valueOf(tipoChave.name)
        },
        chave = chave,
        tipoDeConta = when(tipoConta) {
            TipoConta.UNKNOWN_TIPO_CONTA -> throw IllegalArgumentException("Tipo de conta desconhecida")
            else -> TipoDeConta.valueOf(tipoConta.name)
        }
    )
}