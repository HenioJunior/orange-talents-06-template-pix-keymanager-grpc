package com.zupacademy.henio.pix.carrega

import com.google.protobuf.Timestamp
import com.zupacademy.henio.pix.cliente.bcb.ChavePixInfo
import com.zupacademy.henio.pix.grpc.*
import java.time.ZoneId

class ConsultaChavePixConverter {

    fun convert(chaveInfo: ChavePixInfo): CarregaChavePixResponse {

        return CarregaChavePixResponse.newBuilder()
            .setClienteId(chaveInfo.clienteId.toString())
            .setPixId(chaveInfo.id.toString())
            .setChave(CarregaChavePixResponse.ChavePix.newBuilder()
                .setTipoChave(TipoChave.valueOf(chaveInfo.tipoDeChave.name))
                .setChave(chaveInfo.chave)
                .setConta(
                    CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipoConta(TipoConta.valueOf(chaveInfo.tipoDeConta.name))
                    .setInstituicao(chaveInfo.conta.instituicao)
                    .setNomeDoTitular(chaveInfo.conta.nomeDoTitular)
                    .setCpfDoTitular(chaveInfo.conta.cpfDoTitular)
                    .setAgencia(chaveInfo.conta.agencia)
                    .setNumeroDaConta(chaveInfo.conta.numero)
                    .build()
                )
                .setCreatedAt(chaveInfo.criadaEm.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
            )
            .build()
    }
}