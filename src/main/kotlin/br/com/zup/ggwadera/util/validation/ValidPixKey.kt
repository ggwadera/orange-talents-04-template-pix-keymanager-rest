package br.com.zup.ggwadera.util.validation

import br.com.zup.ggwadera.register.RegisterPixKeyRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.TYPE

@MustBeDocumented
@Retention(RUNTIME)
@Target(CLASS, TYPE)
@Constraint(validatedBy = [ValidPixKeyValidator::class])
@Suppress("unused")
annotation class ValidPixKey(
    val message: String = "Chave Pix inválida para o tipo solicitado",
)

@Singleton
class ValidPixKeyValidator : ConstraintValidator<ValidPixKey, RegisterPixKeyRequest> {
    override fun isValid(
        value: RegisterPixKeyRequest?,
        annotationMetadata: AnnotationValue<ValidPixKey>,
        context: ConstraintValidatorContext
    ): Boolean = value?.keyType == null || value.keyType.isValid(value.key)
}
