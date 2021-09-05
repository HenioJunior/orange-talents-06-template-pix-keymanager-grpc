package com.zupacademy.henio.pix

import com.zupacademy.henio.pix.carrega.Filtro
import com.zupacademy.henio.pix.grpc.CarregaChavePixRequest
import com.zupacademy.henio.pix.remove.RemoveChavePixRequest
import io.micronaut.validation.validator.Validator
import javax.validation.ConstraintViolationException


fun RemoveChavePixRequest.toRemoveRequest(): RemoveChavePixRequest {

    return RemoveChavePixRequest(clienteId, pixId)
}

fun CarregaChavePixRequest.toFiltro(validator: Validator): Filtro {

    val filtro = when(filtroCase!!) {
        CarregaChavePixRequest.FiltroCase.PIXID -> pixId.let {
            Filtro.PorPixId(clienteId = it.clienteId, pixId = it.pixId)
        }
        CarregaChavePixRequest.FiltroCase.CHAVE -> Filtro.PorChave(chave)

        CarregaChavePixRequest.FiltroCase.FILTRO_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if(violations.isNotEmpty()) {
        throw ConstraintViolationException(violations)
    }

    return filtro

}
