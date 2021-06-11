package br.com.zup.ggwadera.list

import br.com.zup.ggwadera.ListKeysServiceGrpc
import br.com.zup.ggwadera.ListPixKeysRequest
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
class ListKeysController(
    private val grpcClient: ListKeysServiceGrpc.ListKeysServiceBlockingStub
) {

    @Get("/{clientId}/keys")
    fun listKeys(@PathVariable @ValidUUID clientId: String): HttpResponse<ListPixKeyResponse> =
        grpcClient.listPixKeys(ListPixKeysRequest.newBuilder().setClientId(clientId).build())
            .let { HttpResponse.ok(ListPixKeyResponse.of(it)) }

}