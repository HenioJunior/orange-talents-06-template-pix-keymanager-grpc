package com.zupacademy.henio.pix.cliente.itau

data class DadosDaContaResponse(
    val tipo: String,
    val instituicao: InstituicaoResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse) {

    fun associaConta(): ContaAssociada {

        return ContaAssociada(
            instituicao = this.instituicao.nome,
            nomeDoTitular = this.titular.nome,
            cpfDoTitular = this.titular.cpf,
            agencia = this.agencia,
            numero = this.numero
        )
    }
}

data class InstituicaoResponse(val nome: String, val ispb: String)
data class TitularResponse(val nome: String, val cpf: String)

