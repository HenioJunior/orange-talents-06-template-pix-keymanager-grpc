package com.zupacademy.henio.pix.exceptions.handlers

import com.zupacademy.henio.pix.exceptions.ExceptionHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandlerResolver(@Inject val handlers: List<ExceptionHandler<Exception>>) {

    private var defaultHandler: ExceptionHandler<Exception> = DefaultExceptionHandler()
    constructor(handlers: List<ExceptionHandler<Exception>>, defaultHandler: ExceptionHandler<Exception>)
            : this(handlers) {
        this.defaultHandler = defaultHandler
    }

    fun resolve(e: Exception): ExceptionHandler<Exception> {

        val foundHandlers = handlers.filter { h -> h.supports(e) }
        if(foundHandlers.size > 1) {
            throw IllegalStateException(
                "Too many handlers supporting ${e.javaClass.name}: $foundHandlers")
        }

        return foundHandlers.firstOrNull() ?: defaultHandler
    }
}