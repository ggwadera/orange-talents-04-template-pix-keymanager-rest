package br.com.zup.ggwadera.register

import br.com.zup.ggwadera.AccountType
import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.NewPixKeyRequest
import br.com.zup.ggwadera.util.validation.ValidPixKey
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class RegisterPixKeyRequest(
    @field:Size(max = 77)
    val key: String?,

    @field:NotNull
    val keyType: KeyTypeRequest?,

    @field:NotNull
    val accountType: AccountTypeRequest?
) {

    fun toGrpc(@NotBlank @ValidUUID clientId: String): NewPixKeyRequest = NewPixKeyRequest.newBuilder()
        .setClientId(clientId)
        .setKey(key ?: "")
        .setKeyType(keyType?.proto ?: KeyType.KEY_TYPE_UNSPECIFIED)
        .setAccountType(accountType?.proto ?: AccountType.ACCOUNT_TYPE_UNSPECIFIED)
        .build()
}

