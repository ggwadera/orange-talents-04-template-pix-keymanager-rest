package br.com.zup.ggwadera.delete

import br.com.zup.ggwadera.DeleteKeyServiceGrpc
import br.com.zup.ggwadera.DeletePixKeyReply
import br.com.zup.ggwadera.GrpcClientFactory
import io.kotest.matchers.shouldBe
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class DeleteKeyControllerTest {
    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class TestFactory {
        @Singleton
        fun stubMock() = Mockito.mock(DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub::class.java)
    }

    @Inject
    lateinit var grpcClient: DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve deletar uma chave pix`() {
        val clientId = "2080de72-8f05-438c-8eed-7c90eb18fb23"
        val pixId = "29eade4e-ff48-4bfb-999f-87e41adfdcfe"

        whenever(grpcClient.deletePixKey(any())).thenReturn(
            DeletePixKeyReply.newBuilder().setClientId(clientId).setPixId(pixId).build()
        )

        val request = HttpRequest.DELETE<Void>("/api/clients/$clientId/keys/$pixId")
        val response = client.toBlocking().exchange(request, Argument.VOID)
        response.status shouldBe HttpStatus.OK
    }

}