package br.com.zup.ggwadera.find

import br.com.zup.ggwadera.AccountType
import br.com.zup.ggwadera.FindKeyServiceGrpc
import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.PixKeyDetailsReply
import io.kotest.assertions.asClue
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@MicronautTest
internal class FindKeyControllerTest {

    companion object {
        private const val clientId = "ea7b9c22-f956-4ba9-a8b0-997d4b2a7f32"
        private const val pixId = "dc726c9d-1166-485b-8233-d4f3f9bdc398"
    }

    @Inject
    lateinit var grpcClient: FindKeyServiceGrpc.FindKeyServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve buscar chave existente`() {

        val protoReply = PixKeyDetailsReply.newBuilder().apply {
            clientId = FindKeyControllerTest.clientId
            pixId = FindKeyControllerTest.pixId
            key = "3e013b10-42b0-4af8-861c-80e8195573a9"
            keyType = KeyType.RANDOM
            owner = ownerBuilder.apply {
                name = "Bill Gates"
                cpf = "01234567890"
            }.build()
            account = accountBuilder.apply {
                name = "ITAÚ UNIBANCO S.A."
                branch = "0001"
                accountNumber = "123456"
                accountType = AccountType.CONTA_CORRENTE
            }.build()
            createdAt = LocalDateTime.now().toInstant(ZoneOffset.UTC)
                .run { createdAtBuilder.setNanos(nano).setSeconds(epochSecond).build() }
        }.build()

        val expected = PixKeyDetailsResponse.of(protoReply)

        whenever(grpcClient.findPixKey(any())).thenReturn(protoReply)

        val request = HttpRequest.GET<Void>("/api/clients/$clientId/keys/$pixId")
        val response = client.toBlocking().exchange(request, Argument.of(PixKeyDetailsResponse::class.java))
        response.asClue {
            it.status shouldBe HttpStatus.OK
            it.body shouldBePresent { this shouldBe expected }
        }
    }
}