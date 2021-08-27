package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.validacoes.ValidaUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class ExcluiRequest(

    @ValidaUUID
    @field:NotBlank
    val clienteId: String?,

    @ValidaUUID
    @field:NotBlank
    val pixId: String?
)