package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.ExcluiChaveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.ExcluiChaveRequest
import com.zupacademy.henio.pix.grpc.ExcluiChaveResponse
import com.zupacademy.henio.pix.grpc.RegistraChaveResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class ExcluiChaveEndpoint(
    @Inject private val service: ExcluiChaveService,) : ExcluiChaveGrpcServiceGrpc.ExcluiChaveGrpcServiceImplBase() {

    override fun exclui(request: ExcluiChaveRequest,
                        responseObserver: StreamObserver<ExcluiChaveResponse>) {

        service.exclui(clienteId = request.clienteId, pixId = request.pixId)

        responseObserver.onNext(ExcluiChaveResponse.newBuilder()
            .setClienteId(request.clienteId)
            .setPixId(request.pixId)
            .build())
        responseObserver.onCompleted()
    }
}