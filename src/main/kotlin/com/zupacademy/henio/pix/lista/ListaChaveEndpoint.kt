package com.zupacademy.henio.pix.lista

import com.google.protobuf.Timestamp
import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.*
import io.grpc.stub.StreamObserver
import java.lang.IllegalArgumentException
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class ListaChaveEndpoint(
    @Inject val repository: ChavePixRepository
): KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceImplBase() {

    override fun lista(request: ListaChavePixRequest,
                      responseObserver: StreamObserver<ListaChavePixResponse>,
    ) {

        if(request.clienteId.isNullOrBlank()) {
            throw IllegalArgumentException("Cliente ID não pode ser nulo ou vazio")
        }

        val lista = repository.findAllByClienteId(UUID.fromString(request.clienteId))
            .map {
                ListaChavePixResponse.ChavePix.newBuilder()
                    .setPixId(it.id.toString())
                    .setTipoChave(TipoChave.valueOf(it.tipoDeChave.name))
                    .setChave(it.chave)
                    .setTipoConta(TipoConta.valueOf(it.tipoDeConta.name))
                    .setCreatedAt(it.criadaEm.let {
                        val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                        Timestamp.newBuilder()
                            .setSeconds(createdAt.epochSecond)
                            .setNanos(createdAt.nano)
                            .build()
                    })
                    .build()
            }
        responseObserver.onNext(ListaChavePixResponse.newBuilder()
            .addAllChaves(lista)
            .build())
        responseObserver.onCompleted()
    }
}