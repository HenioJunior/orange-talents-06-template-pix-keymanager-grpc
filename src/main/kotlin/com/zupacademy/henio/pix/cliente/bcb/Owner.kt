package com.zupacademy.henio.pix.cliente.bcb

data class Owner (
    val type: String,
    val name: String,
    val taxIdNumber: String
) {
    enum class OwnerType {
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}