package com.zupacademy.henio.pix.exceptions.handlers

import com.zupacademy.henio.pix.exceptions.InconsistenciaDeDadosException
import com.zupacademy.henio.pix.exceptions.handlers.ExceptionHandler.*

import io.grpc.Status
import javax.inject.Singleton

@Singleton
class InconsistenciaDeDadosExceptionHandler : ExceptionHandler<InconsistenciaDeDadosException> {

    override fun handle(e: InconsistenciaDeDadosException): StatusWithDetails {
        return StatusWithDetails(
            Status.DATA_LOSS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is InconsistenciaDeDadosException
    }
}