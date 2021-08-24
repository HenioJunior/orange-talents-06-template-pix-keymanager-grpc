package com.zupacademy.henio.pix.validacoes

import com.zupacademy.henio.pix.registra.NovaChavePix
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE
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
class ValidaChavePixValidator: ConstraintValidator<ValidaChavePix, NovaChavePix> {

    override fun isValid(
        value: NovaChavePix?,
        annotationMetadata: AnnotationValue<ValidaChavePix>,
        context: ConstraintValidatorContext
    ): Boolean {

        if(value?.tipo == null) {
            return false
        }

        return value.tipo.valida(value.chave)
    }
}