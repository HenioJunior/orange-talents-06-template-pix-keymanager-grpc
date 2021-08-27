package com.zupacademy.henio.pix.validacoes

import com.zupacademy.henio.pix.registra.NovaChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.*
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Target(CLASS, TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidaChavePixValidator::class])
annotation class ValidaChavePix(
    val message: String = "chave Pix inválida (\${validatedValue.tipo})",
    val groupds: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ValidaChavePixValidator: ConstraintValidator<ValidaChavePix, NovaChaveRequest> {

    override fun isValid(
        value: NovaChaveRequest?,
        annotationMetadata: AnnotationValue<ValidaChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {

        if(value?.tipoChave == null) {
            return false
        }

        return value.tipoChave.valida(value.valorChave)
    }
}