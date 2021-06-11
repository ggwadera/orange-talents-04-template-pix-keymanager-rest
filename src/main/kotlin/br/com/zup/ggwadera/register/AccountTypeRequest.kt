package br.com.zup.ggwadera.register

import br.com.zup.ggwadera.AccountType

enum class AccountTypeRequest(val proto: AccountType) {
    CONTA_CORRENTE(AccountType.CONTA_CORRENTE),
    CONTA_POUPANCA(AccountType.CONTA_POUPANCA)
}