package br.com.zup.ggwadera.register

import br.com.zup.ggwadera.KeyType
import br.com.zup.ggwadera.util.validation.ValidationUtils

enum class KeyTypeRequest(val proto: KeyType) {
    CPF(KeyType.CPF) {
        override fun isValid(key: String?) = ValidationUtils.isCPF(key)
    },
    PHONE(KeyType.PHONE) {
        override fun isValid(key: String?) = ValidationUtils.isPhoneNumber(key)
    },
    EMAIL(KeyType.EMAIL) {
        override fun isValid(key: String?) = ValidationUtils.isEmail(key)
    },
    RANDOM(KeyType.RANDOM) {
        override fun isValid(key: String?) = key.isNullOrBlank()
    };

    abstract fun isValid(key: String?): Boolean
}