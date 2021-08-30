package com.zupacademy.henio.pix.cliente

import com.fasterxml.jackson.annotation.JsonProperty

data class ItauAccountResponse(

    @JsonProperty("nome")
    val bankAccountName: String,

    @JsonProperty("ispb")
    val bankAccountIspb: String,
)