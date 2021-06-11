package br.com.zup.ggwadera.register

import br.com.zup.ggwadera.RegisterKeyServiceGrpc
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Controller(
    value = "/api/clients",
    consumes = [MediaType.APPLICATION_JSON],
    produces = [MediaType.APPLICATION_JSON]
)
@Validated
class RegisterPixKeyController(
    private val grpcClient: RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Post("/{clientId}/keys")
    fun register(
        @PathVariable @ValidUUID clientId: String,
        @Body @Valid request: RegisterPixKeyRequest
    ): HttpResponse<Void> = request
        .also { logger.info("Nova chave para clientId=$clientId $it") }
        .let { grpcClient.registerPixKey(it.toGrpc(clientId)) }
        .run { HttpResponse.uri("/api/clients/$clientId/keys/$pixId") }
        .let { HttpResponse.created(it) }
}