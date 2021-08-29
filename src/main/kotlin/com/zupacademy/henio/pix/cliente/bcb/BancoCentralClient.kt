package com.zupacademy.henio.pix.cliente

import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyRequest
import com.zupacademy.henio.pix.cliente.bcb.CreatePixKeyResponse
import com.zupacademy.henio.pix.cliente.bcb.DeletePixKeyRequest
import com.zupacademy.henio.pix.cliente.bcb.DeletePixKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${bc.pix.url}")
interface BancoCentralClient {

    @Post(
        value = "/api/v1/pix/keys",
        produces = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun cadastraChaveNoBC(@Body request: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete(value = "/api/v1/pix/keys/{key}",
        produces = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML])
    fun excluiChaveNoBC(@PathVariable key: String, @Body request: DeletePixKeyRequest)
            : HttpResponse<DeletePixKeyResponse>

}