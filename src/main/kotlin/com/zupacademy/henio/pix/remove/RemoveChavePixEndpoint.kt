package com.zupacademy.henio.pix.remove

import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.PixKeyDeleteRequest
import com.zupacademy.henio.pix.grpc.PixKeyDeleteResponse
import com.zupacademy.henio.pix.grpc.PixKeyDeleteServiceGrpc
import com.zupacademy.henio.pix.toRemoveRequest
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@ErrorHandler
@Singleton
class RemoveChavePixEndpoint(
    @Inject private val service: RemoveChavePixService,) : PixKeyDeleteServiceGrpc.PixKeyDeleteServiceImplBase() {

    override fun delete(request: PixKeyDeleteRequest,
                        responseObserver: StreamObserver<PixKeyDeleteResponse>) {

        service.exclui(request.clientId, request.pixId)

        responseObserver.onNext(PixKeyDeleteResponse.newBuilder()
            .setClientId(request.clientId)
            .setPixId(request.pixId)
            .build())
        responseObserver.onCompleted()
    }
}