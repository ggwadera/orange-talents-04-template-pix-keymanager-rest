package br.com.zup.ggwadera.util.validation

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

class ValidationUtilsTest {

    @Nested
    inner class CPF {

        @ParameterizedTest
        @NullAndEmptySource
        fun `deve retornar false para valores nulo ou em branco`(value: String?) {
            ValidationUtils.isCPF(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["1", "1234567890", "123456789012", "isso não é um cpf"])
        fun `deve retornar false para valores diferentes de 11 digitos`(value: String?) {
            ValidationUtils.isCPF(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["11111111111", "12312312312", "53235018809", "15040864728"])
        fun `deve retornar false para CPFs invalidos`(value: String?) {
            ValidationUtils.isCPF(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["01234567890", "18318238532", "27508650344"])
        fun `deve retornar true para CPFs validos`(value: String?) {
            ValidationUtils.isCPF(value) shouldBe true
        }

    }

    @Nested
    inner class PhoneNumber {

        @ParameterizedTest
        @NullAndEmptySource
        fun `deve retornar false para valores nulo ou em branco`(value: String?) {
            ValidationUtils.isPhoneNumber(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["11987654321", "987654321", "87654321", "+0011987654321", "isso não é um número de telefone"])
        fun `deve retornar false para numeros invalidos`(value: String?) {
            ValidationUtils.isPhoneNumber(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["+5511987654321", "+1188888888888", "+9911987654321"])
        fun `deve retornar true para numeros validos`(value: String?) {
            ValidationUtils.isPhoneNumber(value) shouldBe true
        }

    }

    @Nested
    inner class Email {

        @ParameterizedTest
        @NullAndEmptySource
        fun `deve retornar false para valores nulo ou em branco`(value: String?) {
            ValidationUtils.isEmail(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["usuario.dominio.com", "usuario@", "@dominio.com", "isso não é um email"])
        fun `deve retornar false para emails invalidos`(value: String?) {
            ValidationUtils.isEmail(value) shouldBe false
        }

        @ParameterizedTest
        @ValueSource(strings = ["usuario@dominio.com", "real@email.org", "guilherme.gwadera@zup.com.br"])
        fun `deve retornar true para emails validos`(value: String?) {
            ValidationUtils.isEmail(value) shouldBe true
        }

    }
}
