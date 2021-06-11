package br.com.zup.ggwadera.list

import br.com.zup.ggwadera.AccountType
import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.ListPixKeysReply
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ListPixKeyResponse(
    val clientId: String,
    val keys: List<PixKeyResponse>
) {
    data class PixKeyResponse(
        val pixId: String,
        val key: String,
        val keyType: KeyType,
        val accountType: AccountType,
        val createdAt: LocalDateTime
    )

    companion object {
        fun of(pixKeys: ListPixKeysReply) = with(pixKeys) {
            ListPixKeyResponse(
                clientId = clientId,
                keys = keysList.map {
                    PixKeyResponse(
                        pixId = it.pixId,
                        key = it.key,
                        keyType = it.keyType,
                        accountType = it.accountType,
                        createdAt = it.createdAt.run { LocalDateTime.ofEpochSecond(seconds, nanos, ZoneOffset.UTC) }
                    )
                }
            )
        }
    }
}
