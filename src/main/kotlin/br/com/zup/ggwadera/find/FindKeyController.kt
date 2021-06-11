package br.com.zup.ggwadera.find

import br.com.zup.ggwadera.FindKeyServiceGrpc
import br.com.zup.ggwadera.FindPixKeyRequest
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated

@Controller(
    value = "/api/clients",
    consumes = [MediaType.APPLICATION_JSON],
    produces = [MediaType.APPLICATION_JSON]
)
@Validated
@Suppress("unused")
class FindKeyController(private val grpcClient: FindKeyServiceGrpc.FindKeyServiceBlockingStub) {

    @Get("/{clientId}/keys/{pixId}")
    fun findKey(
        @PathVariable @ValidUUID clientId: String,
        @PathVariable @ValidUUID pixId: String
    ): HttpResponse<PixKeyDetailsResponse> = grpcClient.findPixKey(
        FindPixKeyRequest.newBuilder()
            .setIds(FindPixKeyRequest.ById.newBuilder()
                .setClientId(clientId)
                .setPixId(pixId))
            .build()
    ).let { HttpResponse.ok(PixKeyDetailsResponse.of(it)) }

}