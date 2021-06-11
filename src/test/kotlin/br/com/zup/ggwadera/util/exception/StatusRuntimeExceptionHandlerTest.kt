package br.com.zup.ggwadera.util.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class StatusRuntimeExceptionHandlerTest {

    private val exceptionHandler = StatusRuntimeExceptionHandler()

    @ParameterizedTest
    @MethodSource("exceptionProvider")
    internal fun `deve mapear excecao corretamente`(exception: StatusRuntimeException, expected: HttpStatus) {
        exceptionHandler.handle(request, exception).status shouldBe expected
    }

    @ParameterizedTest
    @MethodSource("exceptionWithDescriptionProvider")
    internal fun `deve mapear excecao com descricao corretamente`(exception: StatusRuntimeException, expected: String) {
        exceptionHandler.handle(request, exception).body shouldBePresent {
            this.toString() shouldBe when (exception.status.code) {
                Status.Code.INTERNAL -> "erro inesperado ao processar solicitação: ${Status.Code.INTERNAL} - $expected"
                else -> expected
            }
        }
    }

    companion object {

        private val request: HttpRequest<Any> = HttpRequest.GET<Any>("http://fake.uri")

        private val codeMap = mapOf(
            Status.Code.ALREADY_EXISTS to HttpStatus.UNPROCESSABLE_ENTITY,
            Status.Code.NOT_FOUND to HttpStatus.NOT_FOUND,
            Status.Code.INVALID_ARGUMENT to HttpStatus.BAD_REQUEST,
            Status.Code.FAILED_PRECONDITION to HttpStatus.UNPROCESSABLE_ENTITY,
            Status.Code.INTERNAL to HttpStatus.INTERNAL_SERVER_ERROR
        )

        private fun getStatusRuntimeException(code: Status.Code, description: String = ""): StatusRuntimeException =
            Status.fromCode(code)
                .let { if (description.isNotBlank()) it.withDescription(description) else it }
                .asRuntimeException()

        @JvmStatic
        fun exceptionProvider(): Collection<Arguments> =
            codeMap.map { Arguments.of(getStatusRuntimeException(it.key), it.value) }

        @JvmStatic
        fun exceptionWithDescriptionProvider(): Collection<Arguments> =
            codeMap.map { Arguments.of(getStatusRuntimeException(it.key, "description"), "description") }
    }


    //    "deve mapear a exceção corretamente" {
//        codeMap.map { row(getStatusRuntimeException(it.key), it.value) }
//            .map { (exception, expected) ->
//                exceptionHandler.handle(request, exception).status shouldBe expected
//            }
//    }
//
//    "deve mapear a exceção com descrição corretamente" {
//        codeMap.map { row(getStatusRuntimeException(it.key, "descrição"), "descrição") }
//            .map { (exception, expected) ->
//                exceptionHandler.handle(request, exception).body shouldBePresent {
//                    this.toString() shouldBe when (exception.status.code) {
//                        Status.Code.INTERNAL -> "erro inesperado ao processar solicitação: ${Status.Code.INTERNAL} - $expected"
//                        else -> expected
//                    }
//                }
//            }
//    }

}