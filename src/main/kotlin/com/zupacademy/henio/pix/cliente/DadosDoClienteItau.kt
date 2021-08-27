package com.zupacademy.henio.pix.cliente

import com.zupacademy.henio.pix.registra.InstituicaoResponse

class DadosDoClienteResponse (

    val id: String,
    val nome: String,
    val cpf: String,
    val instituicao: InstituicaoResponse
)