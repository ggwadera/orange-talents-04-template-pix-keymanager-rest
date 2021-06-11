package br.com.zup.ggwadera.list

import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.ListKeysServiceGrpc
import br.com.zup.ggwadera.ListPixKeysReply
import com.google.protobuf.Timestamp
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
import java.time.Instant
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class ListKeysControllerTest {

    companion object {
        private const val clientId = "ea7b9c22-f956-4ba9-a8b0-997d4b2a7f32"
    }

    @Inject
    lateinit var grpcClient: ListKeysServiceGrpc.ListKeysServiceBlockingStub

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve listar chaves do usuario`() {
        val listProto =
            ListPixKeysReply.newBuilder()
                .setClientId(clientId)
                .addAllKeys(
                    listOf(
                        pixKeyProto("01234567890", KeyType.CPF),
                        pixKeyProto("fake@email.com", KeyType.EMAIL),
                        pixKeyProto(UUID.randomUUID().toString(), KeyType.RANDOM)
                    )
                )
                .build()

        val expected = ListPixKeyResponse.of(listProto)

        whenever(grpcClient.listPixKeys(any())).thenReturn(listProto)

        val request = HttpRequest.GET<Void>("/api/clients/$clientId/keys")
        val response = client.toBlocking().exchange(request, Argument.of(ListPixKeyResponse::class.java))
        response.asClue {
            it.status shouldBe HttpStatus.OK
            it.body shouldBePresent { this shouldBe expected }
        }
    }

    private fun pixKeyProto(key: String, keyType: KeyType) = ListPixKeysReply.PixKey.newBuilder()
        .setKey(key)
        .setKeyType(keyType)
        .setPixId(UUID.randomUUID().toString())
        .setCreatedAt(with(Instant.now()) { Timestamp.newBuilder().setNanos(nano).setSeconds(epochSecond).build() })
        .build()
}
