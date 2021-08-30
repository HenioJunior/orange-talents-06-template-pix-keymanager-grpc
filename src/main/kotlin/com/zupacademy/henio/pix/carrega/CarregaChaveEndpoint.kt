package com.zupacademy.henio.pix.carrega

import com.zupacademy.henio.pix.chave.ChavePixRepository
import com.zupacademy.henio.pix.cliente.bcb.BancoCentralClient
import com.zupacademy.henio.pix.exceptions.handlers.ErrorHandler
import com.zupacademy.henio.pix.grpc.PixKeyGetRequest
import com.zupacademy.henio.pix.grpc.PixKeyGetResponse
import com.zupacademy.henio.pix.grpc.PixKeyGetServiceGrpc
import com.zupacademy.henio.pix.toFiltro
import io.grpc.stub.StreamObserver
import io.micronaut.validation.validator.Validator
import javax.inject.Inject
import javax.inject.Singleton


@ErrorHandler
@Singleton
class CarregaChaveEndpoint(
    @Inject private val repository: ChavePixRepository,
    @Inject private val bcbClient: BancoCentralClient,
    @Inject private val validator: Validator
): PixKeyGetServiceGrpc.PixKeyGetServiceImplBase() {


    override fun get(
        request: PixKeyGetRequest,
        responseObserver: StreamObserver<PixKeyGetResponse>
    ) {
        val filtro = request.toFiltro(validator)
        val chaveInfo = filtro.filtra(repository, bcbClient)

        responseObserver.onNext(ConsultaChavePixConverter().convert(chaveInfo))
        responseObserver.onCompleted()

    }
}


