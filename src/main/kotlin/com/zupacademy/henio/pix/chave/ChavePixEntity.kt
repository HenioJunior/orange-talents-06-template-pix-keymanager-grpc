package com.zupacademy.henio.pix.chave

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class ChavePixEntity(
    @field: NotNull
    @Column(nullable = false)
    val clienteId: UUID,

    @Enumerated(EnumType.STRING)
    @field: NotNull
    @Column(nullable = false)
    val tipoChave: TipoDeChave,

    @field: NotBlank
    @Column(unique = true, nullable = false)
    var chave: String,

    @Enumerated(EnumType.STRING)
    @field: NotNull
    @Column(nullable = false)
    val tipoConta: TipoDeConta,

    @field:Valid
    @Embedded
    val conta: ContaAssociada,
) {
    @Id
    @GeneratedValue
    val id: UUID? = null

    @Column(nullable = false)
    val criadaEm: LocalDateTime = LocalDateTime.now()

    fun eDoCliente(idCliente: String): Boolean {
        return UUID.fromString(idCliente) == this.clienteId
    }

}