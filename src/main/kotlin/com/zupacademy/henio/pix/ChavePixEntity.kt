package com.zupacademy.henio.pix

import com.zupacademy.henio.pix.registra.ContaAssociada
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "uk_chave_pix",
columnNames = ["chave"]
)])
class ChavePixEntity(
    @field: NotNull
    @Column(nullable = false)
    val clienteId: UUID,

    @Enumerated(EnumType.STRING)
    @field: NotNull
    @Column(nullable = false)
    val tipo: TipoDeChave,

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

    override fun toString(): String {
        return "ChavePix(clienteId=$clienteId, tipo=$tipo, chave='$chave', tipoDeConta=$tipoDeConta, conta=$conta, id=$id)"
    }
}