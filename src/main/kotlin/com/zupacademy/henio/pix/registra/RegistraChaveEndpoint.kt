package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.RegistraChaveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.RegistraChaveRequest
import com.zupacademy.henio.pix.grpc.RegistraChaveResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RegistraChaveEndpoint(@Inject private val service: RegistraChaveService)
    : RegistraChaveGrpcServiceGrpc.RegistraChaveGrpcServiceImplBase() {

    override fun registra(
        grpcRequest: RegistraChaveRequest,
        responseObserver: StreamObserver<RegistraChaveResponse>
    ) {
        val novaChave = grpcRequest.toDTO();
        val chaveCriada = service.registra(novaChave)

        responseObserver.onNext(
            RegistraChaveResponse.newBuilder()
                .setClienteId(chaveCriada.clienteId.toString())
                .setPixId(chaveCriada.id.toString())
                .build())

        responseObserver.onCompleted()
    }
}


