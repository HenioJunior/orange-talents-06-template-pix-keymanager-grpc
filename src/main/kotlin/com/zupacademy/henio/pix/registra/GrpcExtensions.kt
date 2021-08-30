package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.grpc.AccountType
import com.zupacademy.henio.pix.grpc.KeyType
import com.zupacademy.henio.pix.grpc.PixKeyDeleteRequest
import com.zupacademy.henio.pix.grpc.PixKeyRequest
import com.zupacademy.henio.pix.remove.RemoveChavePixRequest

fun PixKeyRequest.toDto(): NovaChavePixRequest {

    return NovaChavePixRequest(
        clienteId = clientId,
        tipoDeChave = when(keyType) {
            KeyType.UNKNOWN_KEY -> null
            else -> TipoDeChave.valueOf(keyType.name)
        },
        valorDaChave = key,
        tipoDeConta = when(accountType) {
            AccountType.UNKNOWN_ACCOUNT -> null
            else -> TipoDeConta.valueOf(accountType.name)
        }
    )
}

fun PixKeyDeleteRequest.toRemoveRequest(): RemoveChavePixRequest {

    return RemoveChavePixRequest(clientId, pixId)
}