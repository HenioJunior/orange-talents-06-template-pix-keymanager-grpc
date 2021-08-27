package com.zupacademy.henio.pix.registra

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.RegistraChaveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.RegistraChaveRequest
import com.zupacademy.henio.pix.grpc.RegistraChaveResponse
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@ErrorHandler
@Singleton
class RegistraChaveEndpoint(@Inject private val service: NovaChaveService)
    : RegistraChaveGrpcServiceGrpc.RegistraChaveGrpcServiceImplBase() {

    override fun registra(
        grpcRequest: RegistraChaveRequest,
        responseObserver: StreamObserver<RegistraChaveResponse>
    ) {

        try {
            val novaChave = grpcRequest.toModel();
            val chaveCriada = service.registra(novaChave)
            responseObserver.onNext(
                RegistraChaveResponse.newBuilder()
                    .setClienteId(chaveCriada.clienteId.toString())
                    .setPixId(chaveCriada.id.toString())
                    .build())

        } catch(e: ConstraintViolationException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("dados de entrada invalidos")
                    .asRuntimeException())
        }
        responseObserver.onCompleted()
    }
}


