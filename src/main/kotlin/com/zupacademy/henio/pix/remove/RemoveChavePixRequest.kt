package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.validacoes.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class RemoveChavePixRequest(

    @ValidUUID
    @field:NotBlank
    val clienteId: String?,

    @ValidUUID
    @field:NotBlank
    val pixId: String?
)