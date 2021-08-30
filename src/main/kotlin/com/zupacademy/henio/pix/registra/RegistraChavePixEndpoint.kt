package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.PixKeyRegisterServiceGrpc
import com.zupacademy.henio.pix.grpc.PixKeyRequest
import com.zupacademy.henio.pix.grpc.PixKeyResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RegistraChavePixEndpoint(@Inject private val service: NovaChavePixService)
    : PixKeyRegisterServiceGrpc.PixKeyRegisterServiceImplBase() {

    override fun register(
        grpcRequest: PixKeyRequest,
        responseObserver: StreamObserver<PixKeyResponse>
    ) {

        val novaChave = grpcRequest.toDto();
        val chaveCriada = service.registra(novaChave)
        responseObserver.onNext(
            PixKeyResponse.newBuilder()
                .setClientId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build())
        responseObserver.onCompleted()
    }
}


