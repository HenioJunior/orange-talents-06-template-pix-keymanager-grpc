package com.zupacademy.henio.pix.registra

import io.micronaut.data.annotation.Embeddable

@Embeddable
data class ContaAssociada(
    val instituicao: String = "",
    val nomeDoTitular: String = "",
    val cpfDoTitular: String = "",
    val agencia: String = "",
    val numero: String = ""
)

