package com.zupacademy.henio.pix.carrega

import com.google.protobuf.Timestamp
import com.zupacademy.henio.pix.cliente.bcb.ChavePixInfo
import com.zupacademy.henio.pix.grpc.AccountType
import com.zupacademy.henio.pix.grpc.KeyType
import com.zupacademy.henio.pix.grpc.PixKeyGetResponse
import java.time.ZoneId

class ConsultaChavePixConverter {

    fun convert(chaveInfo: ChavePixInfo): PixKeyGetResponse {

        return PixKeyGetResponse.newBuilder()
            .setClientId(chaveInfo.clienteId.toString())
            .setPixId(chaveInfo.id.toString())
            .setKey(PixKeyGetResponse.PixKey.newBuilder()
                .setType(KeyType.valueOf(chaveInfo.tipoDeChave.name))
                .setKey(chaveInfo.chave)
                .setAccount(
                    PixKeyGetResponse.PixKey.AccountInfo.newBuilder()
                    .setAccountType(AccountType.valueOf(chaveInfo.tipoDeConta.name))
                    .setParticipant(chaveInfo.conta.instituicao)
                    .setName(chaveInfo.conta.nomeDoTitular)
                    .setTaxIdNumber(chaveInfo.conta.cpfDoTitular)
                    .setBranch(chaveInfo.conta.agencia)
                    .setAccountNumber(chaveInfo.conta.numero)
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