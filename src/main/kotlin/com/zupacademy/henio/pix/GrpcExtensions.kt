package com.zupacademy.henio.pix

import com.zupacademy.henio.pix.carrega.Filtro
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.grpc.*
import com.zupacademy.henio.pix.grpc.PixKeyGetRequest.FilterCase.*
import com.zupacademy.henio.pix.registra.NovaChavePixRequest
import com.zupacademy.henio.pix.remove.RemoveChavePixRequest
import io.micronaut.validation.validator.Validator
import javax.validation.ConstraintViolationException

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

fun PixKeyGetRequest.toFiltro(validator: Validator): Filtro {
    val filtro = when(filterCase) {
        PIXID -> pixId.let {
            Filtro.PorPixId(clienteId = it.clientId, pixId = it.pixId)
        }
        CHAVE -> Filtro.PorChave(chave)
        FILTER_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if(violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }

    return filtro

}
