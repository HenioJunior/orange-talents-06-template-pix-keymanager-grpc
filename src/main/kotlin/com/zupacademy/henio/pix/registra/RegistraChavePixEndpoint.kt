package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.KeymanagerRegistraGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.RegistraChavePixRequest
import com.zupacademy.henio.pix.grpc.RegistraChavePixResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RegistraChavePixEndpoint(@Inject private val service: NovaChavePixService)
    : KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceImplBase() {

    override fun registra(
        grpcRequest: RegistraChavePixRequest,
        responseObserver: StreamObserver<RegistraChavePixResponse>
    ) {

        val novaChave = grpcRequest.toDto()
        val chaveCriada = service.registra(novaChave)

    responseObserver.onNext(RegistraChavePixResponse.newBuilder()
                .setClienteId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build())
        responseObserver.onCompleted()
    }
}


