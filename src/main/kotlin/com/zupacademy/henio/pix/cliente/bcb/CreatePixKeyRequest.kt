package com.zupacademy.henio.pix.cliente.bcb

data class CreatePixKeyRequest(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
)