package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.grpc.ExcluiChaveRequest
import com.zupacademy.henio.pix.grpc.RegistraChaveRequest
import com.zupacademy.henio.pix.grpc.TipoChave.UNKNOWN_TIPO_CHAVE
import com.zupacademy.henio.pix.grpc.TipoConta.UNKNOWN_TIPO_CONTA
import com.zupacademy.henio.pix.remove.RemoveChaveRequest

fun RegistraChaveRequest.toModel(): NovaChaveRequest {

    return NovaChaveRequest(
        clienteId = clienteId,
        tipoChave = when(tipoChave) {
            UNKNOWN_TIPO_CHAVE -> null
            else -> TipoDeChave.valueOf(tipoChave.name)
        },
        valorChave = valorChave,
        tipoConta = when(tipoConta) {
            UNKNOWN_TIPO_CONTA -> null
            else -> TipoDeConta.valueOf(tipoConta.name)
        }
    )
}

fun ExcluiChaveRequest.toRemoveRequest(): RemoveChaveRequest {

    return RemoveChaveRequest(clienteId, pixId)
}