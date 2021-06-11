package br.com.zup.ggwadera.register

import br.com.zup.ggwadera.GrpcClientFactory
import br.com.zup.ggwadera.NewPixKeyReply
import br.com.zup.ggwadera.RegisterKeyServiceGrpc
import io.kotest.assertions.asClue
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
internal class RegisterPixKeyControllerTest {

    @Factory
    @Replaces(factory = GrpcClientFactory::class)
    internal class TestFactory {
        @Singleton
        fun stubMock() = Mockito.mock(RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub::class.java)
    }

    @Inject
    lateinit var grpcClient: RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registar uma chave pix`() {
        val clientId = "7552d284-8bf3-4180-9636-d4c24a5f2542"
        val pixId = "d1627b85-65ad-42fb-9ce1-578fd085e047"
        val request: RegisterPixKeyRequest = buildRequest()

        whenever(grpcClient.registerPixKey(any())).thenReturn(NewPixKeyReply.newBuilder().setPixId(pixId).build())

        val response =
            client.toBlocking().exchange(HttpRequest.POST("/api/clients/$clientId/keys", request), Argument.VOID)
        response.asClue {
            it.status shouldBe HttpStatus.CREATED
            it.header("Location") shouldBe "/api/clients/$clientId/keys/$pixId"
        }
    }

    private fun buildRequest(
        key: String? = null,
        keyType: KeyTypeRequest? = null
    ) = RegisterPixKeyRequest(
        key = key ?: "fake@email.com",
        keyType = keyType ?: KeyTypeRequest.EMAIL,
        accountType = AccountTypeRequest.CONTA_CORRENTE
    )

}
