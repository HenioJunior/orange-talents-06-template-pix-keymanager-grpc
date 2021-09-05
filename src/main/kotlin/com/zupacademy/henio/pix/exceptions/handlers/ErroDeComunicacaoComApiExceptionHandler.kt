package com.zupacademy.henio.pix.exceptions.handlers

import com.zupacademy.henio.pix.exceptions.ErroDeComunicacaoComApiException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ErroDeComunicacaoComApiExceptionHandler : ExceptionHandler<ErroDeComunicacaoComApiException> {

    override fun handle(e: ErroDeComunicacaoComApiException): ExceptionHandler.StatusWithDetails {
        return ExceptionHandler.StatusWithDetails(
            Status.UNKNOWN
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ErroDeComunicacaoComApiException
    }
}