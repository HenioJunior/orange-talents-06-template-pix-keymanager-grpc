package com.zupacademy.henio.pix.chave

import com.zupacademy.henio.pix.cliente.CreatePixKeyRequest
import com.zupacademy.henio.pix.cliente.CreatePixKeyResponse
import com.zupacademy.henio.pix.cliente.itau.ContaAssociada
import com.zupacademy.henio.pix.registra.NovaChavePixRequest
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class ChavePix(
    @field: NotNull
    @Column(nullable = false)
    val clienteId: UUID,

    @Enumerated(EnumType.STRING)
    @field: NotNull
    @Column(nullable = false)
    val tipoDeChave: TipoDeChave,

    @field: NotBlank
    @Column(unique = true, nullable = false)
    var chave: String,

    @Enumerated(EnumType.STRING)
    @field: NotNull
    @Column(nullable = false)
    val tipoDeConta: TipoDeConta,

    @field:Valid
    @Embedded
    val conta: ContaAssociada,
) {


    @Id
    @GeneratedValue
    val id: UUID? = null

    @Column(nullable = false)
    val criadaEm: LocalDateTime = LocalDateTime.now()

}