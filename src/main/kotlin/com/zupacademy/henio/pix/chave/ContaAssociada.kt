package com.zupacademy.henio.pix.chave

import javax.persistence.Embeddable


@Embeddable
data class ContaAssociada(
    val instituicao: String,
    val nomeDoTitular: String,
    val cpfDoTitular: String,
    val agencia: String,
    val numero: String
)

