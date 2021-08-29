package com.zupacademy.henio.pix.exceptions.handlers

import com.zupacademy.henio.pix.exceptions.ClienteNaoEDonoDaChaveException
import com.zupacademy.henio.pix.exceptions.handlers.ExceptionHandler.*
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ClienteNaoEDonoDaChaveExceptionHandler : ExceptionHandler<ClienteNaoEDonoDaChaveException> {

    override fun handle(e: ClienteNaoEDonoDaChaveException): StatusWithDetails {
        return StatusWithDetails(
            Status.PERMISSION_DENIED
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is ClienteNaoEDonoDaChaveException
    }
}