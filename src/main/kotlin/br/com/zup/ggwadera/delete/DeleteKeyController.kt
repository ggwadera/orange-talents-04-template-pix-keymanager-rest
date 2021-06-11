package br.com.zup.ggwadera.delete

import br.com.zup.ggwadera.DeleteKeyServiceGrpc
import br.com.zup.ggwadera.DeletePixKeyRequest
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated

@Controller(
    value = "/api/clients",
    consumes = [MediaType.APPLICATION_JSON],
    produces = [MediaType.APPLICATION_JSON]
)
@Validated
class DeleteKeyController(
    private val grpcClient: DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub
) {

    @Delete("/{clientId}/keys/{pixId}")
    fun deleteKey(
        @PathVariable @ValidUUID clientId: String,
        @PathVariable @ValidUUID pixId: String
    ): HttpResponse<Void> {
        grpcClient.deletePixKey(DeletePixKeyRequest.newBuilder().setClientId(clientId).setPixId(pixId).build())
        return HttpResponse.ok()
    }
}