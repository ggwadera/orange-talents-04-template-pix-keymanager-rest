package br.com.zup.ggwadera.find

import br.com.zup.ggwadera.AccountType
import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.PixKeyDetailsReply
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
data class PixKeyDetailsResponse(
    val pixId: String,
    val clientId: String,
    val key: String,
    val keyType: KeyType,
    val owner: OwnerResponse,
    val account: AccountResponse,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(pixKey: PixKeyDetailsReply) = with(pixKey) {
            PixKeyDetailsResponse(
                pixId = pixId,
                clientId = clientId,
                key = key,
                keyType = keyType,
                owner = with(owner) { OwnerResponse(name, cpf) },
                account = with(account) { AccountResponse(name, branch, accountNumber, accountType) },
                createdAt = with(createdAt) { LocalDateTime.ofEpochSecond(seconds, nanos, ZoneOffset.UTC) }
            )
        }
    }
}

@Introspected
data class OwnerResponse(val name: String, val cpf: String)

@Introspected
data class AccountResponse(val name: String, val branch: String, val number: String, val type: AccountType)
