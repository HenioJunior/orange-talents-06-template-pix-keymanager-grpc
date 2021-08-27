package com.zupacademy.henio.pix
import com.zupacademy.henio.pix.chave.*
import com.zupacademy.henio.pix.grpc.ExcluiChaveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.ExcluiChaveRequest
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class ExcluiChaveEndpointTest (
    val repository: ChavePixRepository,
    val grpcClient: ExcluiChaveGrpcServiceGrpc.ExcluiChaveGrpcServiceBlockingStub
){

    lateinit var CHAVE_EXISTENTE: ChavePixEntity


    @BeforeEach
    fun setup() {
        repository.deleteAll()

        CHAVE_EXISTENTE = ChavePixEntity(
            clienteId = UUID.fromString("c56dfef4-7901-44fb-84e2-a2cefb157890"),
            tipoChave = TipoDeChave.EMAIL,
            chave = "rafael.ponte@zup.com.br",
            tipoConta = TipoDeConta.CONTA_CORRENTE,
            conta = ContaAssociada(
                instituicao = "ITAÚ UNIBANCO S.A.",
                nomeDoTitular = "Rafael M C Ponte",
                cpfDoTitular = "02467781054",
                agencia = "0001",
                numero = "291900"
            )
        )
        repository.save(CHAVE_EXISTENTE)
    }

    @AfterEach
    fun cleanUp() {
        repository.deleteAll()
    }

    @Test
    fun `deve excluir chave existente`() {
        val response = grpcClient.exclui(
            ExcluiChaveRequest.newBuilder()
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .build())

        with(response) {
            assertEquals(CHAVE_EXISTENTE.id.toString(), response.pixId)
            assertEquals(CHAVE_EXISTENTE.clienteId.toString(), response.clienteId)
        }
    }

    @Test
    fun `nao deve remover chave pix quando chave inexistente`() {
        val pixIdNaoExiste = UUID.randomUUID().toString()

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.exclui(ExcluiChaveRequest.newBuilder()
                .setPixId(pixIdNaoExiste)
                .setClienteId(CHAVE_EXISTENTE.clienteId.toString())
                .build())
        }

        with(excecao) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave Pix não encontrada ou não pertence ao cliente", status.description)
        }
    }

    @Test
    fun `nao deve remover chave pix quando chave existente mas pertence a outro cliente` () {
        val outroCliente = UUID.randomUUID().toString()

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.exclui(ExcluiChaveRequest.newBuilder()
                .setPixId(CHAVE_EXISTENTE.id.toString())
                .setClienteId(outroCliente)
                .build())
        }

        with(excecao) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave Pix não encontrada ou não pertence ao cliente", status.description)
        }
    }
}




@Factory
class Clients {
    @Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            ExcluiChaveGrpcServiceGrpc.ExcluiChaveGrpcServiceBlockingStub{
        return ExcluiChaveGrpcServiceGrpc.newBlockingStub(channel)
    }
}
