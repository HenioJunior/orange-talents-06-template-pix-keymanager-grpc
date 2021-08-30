package com.zupacademy.henio.pix.cliente.bcb

import com.zupacademy.henio.pix.chave.ChavePix
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.cliente.itau.ContaAssociada
import java.time.LocalDateTime
import java.util.*

class ChavePixInfo(
    val clienteId: UUID?,
    val id: UUID?,
    val criadaEm: LocalDateTime,
    val tipoDeChave: TipoDeChave,
    val chave: String,
    val tipoDeConta: TipoDeConta,
    val conta: ContaAssociada
) {
    companion object {
        fun of(chavePix: ChavePix): ChavePixInfo {

            return ChavePixInfo(
                clienteId = chavePix.clienteId,
                id = chavePix.id!!,
                criadaEm = chavePix.criadaEm,
                tipoDeChave = chavePix.tipoDeChave,
                chave = chavePix.chave,
                tipoDeConta = chavePix.tipoDeConta,
                conta = chavePix.conta
            )
        }
    }





}
