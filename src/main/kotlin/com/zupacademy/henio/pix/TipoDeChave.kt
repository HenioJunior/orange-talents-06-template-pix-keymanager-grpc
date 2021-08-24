package com.zupacademy.henio.pix

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator

enum class TipoDeChave {

    CPF {
        override fun valida(chave: String?): Boolean {

            return chave.isNullOrBlank()
        }
    },
    PHONE {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }

            return chave.matches("^\\+[1-9][0-9]\\d{11}\$".toRegex())
        }
    },
    EMAIL {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }

            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }
        }
    },
    RANDOM {
        override fun valida(chave: String?): Boolean {

            return chave.isNullOrBlank()
        }
    };

    abstract fun valida(chave: String?): Boolean
}