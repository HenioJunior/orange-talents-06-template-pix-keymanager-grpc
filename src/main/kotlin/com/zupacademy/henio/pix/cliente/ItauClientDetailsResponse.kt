package com.zupacademy.henio.pix.cliente

import com.fasterxml.jackson.annotation.JsonProperty

data class ItauClientDetailsResponse(
    val id: String,
    @JsonProperty("nome")
    val name: String,
    val cpf: String,
    @JsonProperty("instituicao")
    val account: ItauAccountResponse
){
    fun getAccountIspb(): String {
        return account.bankAccountIspb
    }
}
