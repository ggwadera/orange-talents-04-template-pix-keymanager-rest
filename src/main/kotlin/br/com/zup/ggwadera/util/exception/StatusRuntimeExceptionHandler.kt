package br.com.zup.ggwadera.util.exception

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@Requirements(Requires(classes = [StatusRuntimeException::class, ExceptionHandler::class]))
@Suppress("unused")
class StatusRuntimeExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<*>> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<*> {
        val (httpStatus, message) = with(exception.status) {
            logger.error("Erro ao processar solicitação: $code - $description", exception)
            when (code) {
                Status.Code.ALREADY_EXISTS -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description ?: "recurso já existe")
                Status.Code.INVALID_ARGUMENT -> Pair(HttpStatus.BAD_REQUEST, description ?: "dados inválidos")
                Status.Code.FAILED_PRECONDITION -> Pair(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    description ?: "não foi possível processar a requisição"
                )
                Status.Code.NOT_FOUND -> Pair(HttpStatus.NOT_FOUND, description ?: "recurso não encontrado")
                else -> Pair(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "erro inesperado ao processar solicitação: $code - $description"
                )
            }
        }
        return HttpResponse.status<JsonError>(httpStatus).body(JsonError(message))
    }
}