package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.KeymanagerRemoveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.RemoveChavePixRequest
import com.zupacademy.henio.pix.grpc.RemoveChavePixResponse
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RemoveChavePixEndpoint(
    @Inject private val service: RemoveChavePixService)
    : KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceImplBase() {

    override fun remove(request: RemoveChavePixRequest, responseObserver: StreamObserver<RemoveChavePixResponse>) {

            service.exclui(request.clienteId, request.pixId)

            responseObserver.onNext(RemoveChavePixResponse.newBuilder()
                .setClienteId(request.clienteId)
                .setPixId(request.pixId)
                .build())
            responseObserver.onCompleted()
        }
    }



