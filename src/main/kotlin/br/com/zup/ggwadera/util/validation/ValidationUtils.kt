package br.com.zup.ggwadera.util.validation

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.EmailValidator

object ValidationUtils {

    private const val CPF_PATTERN = "^[0-9]{11}\$"
    private val CPF_REGEX = CPF_PATTERN.toRegex()
    private val CPF_REPEATED_DIGITS = (0..9).map { it.toString().repeat(11) }

    private const val PHONE_PATTERN = "^\\+[1-9][0-9]\\d{1,14}\$"
    private val PHONE_REGEX = PHONE_PATTERN.toRegex()

    /**
     * Valida número de CPF
     * Fonte: https://gist.github.com/trfiladelfo/92edd1cad568ae6bae6c026dac52dff2
     */
    fun isCPF(document: String?): Boolean {
        // primeira validação para verificar se contém 11 dígitos
        if (document.isNullOrBlank()) return false
        if (!document.matches(CPF_REGEX)) return false

        // valida dígitos repetidos
        if (document in CPF_REPEATED_DIGITS) return false

        val numbers = document.split("")
            .filter { it.isNotBlank() }
            .map { it.toInt() }

        // digito verificador 1
        val dv1 = (0..8).sumBy { (it + 1) * numbers[it] }.rem(11).let {
            if (it >= 10) 0 else it
        }

        // digito verificador 1
        val dv2 = (0..8).sumBy { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }.let {
            if (it >= 10) 0 else it
        }

        return numbers[9] == dv1 && numbers[10] == dv2
    }

    fun isPhoneNumber(phoneNumber: String?): Boolean = !phoneNumber.isNullOrBlank() && phoneNumber.matches(PHONE_REGEX)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun isEmail(email: String?): Boolean = !email.isNullOrBlank() && EmailValidator().run{
        initialize(null)
        isValid(email, AnnotationValue("Email"), null)
    }
}