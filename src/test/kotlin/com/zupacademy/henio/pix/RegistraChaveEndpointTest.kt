package com.zupacademy.henio.pix.registra
import com.zupacademy.henio.pix.chave.*
import com.zupacademy.henio.pix.cliente.ContasDeClientesNoItau
import com.zupacademy.henio.pix.grpc.RegistraChaveGrpcServiceGrpc
import com.zupacademy.henio.pix.grpc.RegistraChaveRequest
import com.zupacademy.henio.pix.grpc.TipoChave
import com.zupacademy.henio.pix.grpc.TipoConta
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class RegistraChaveEndpointTest(
    val repository: ChavePixRepository,
    val grpcClient: RegistraChaveGrpcServiceGrpc.RegistraChaveGrpcServiceBlockingStub
) {

    @Inject
    lateinit var itauClient: ContasDeClientesNoItau

    companion object {
        val CLIENTE_ID = "c56dfef4-7901-44fb-84e2-a2cefb157890"
        val CLIENTE_INEXISTENTE = UUID.randomUUID()
        val contaAssociada = ContaAssociada(
            instituicao = "ITAÚ UNIBANCO S.A.",
            nomeDoTitular = "Rafael M C Ponte",
            cpfDoTitular = "02467781054",
            agencia = "0001",
            numero = "291900")
    }

    @BeforeEach
    fun setup() {
        repository.deleteAll()
    }

    @Test
    fun `deve registrar nova chave pix`() {

        Mockito.`when`(itauClient()!!.buscaContaPorTipo(clienteId = CLIENTE_ID.toString(), TipoConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        val response = grpcClient.registra(
            RegistraChaveRequest.newBuilder()
            .setClienteId(CLIENTE_ID.toString())
            .setTipoChave(TipoChave.EMAIL)
            .setValorChave("rponte@gmail.com")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build())

        with(response) {
            assertEquals(CLIENTE_ID.toString(), clienteId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando chave existente`() {

        Mockito.`when`(itauClient()!!.buscaContaPorTipo(CLIENTE_ID, TipoDeConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))

        repository.save(
            ChavePixEntity(
            clienteId = UUID.fromString(CLIENTE_ID),
            tipoChave = TipoDeChave.EMAIL,
            chave = "rponte@gmail.com",
            tipoConta = TipoDeConta.CONTA_CORRENTE,
            conta = contaAssociada
        )
        )

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.registra(RegistraChaveRequest.newBuilder()
                .setClienteId(CLIENTE_ID)
                .setTipoChave(TipoChave.EMAIL)
                .setValorChave("rponte@gmail.com")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .build())
        }
        with(excecao) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave Pix rponte@gmail.com existente", status.description)
        }

    }

    @Test
    fun `nao deve registrar chave pix quando nao encontrar dados da conta cliente` () {

        `when`(itauClient()!!.buscaContaPorTipo(CLIENTE_INEXISTENTE.toString(), TipoConta.CONTA_CORRENTE.toString()))
            .thenReturn(HttpResponse.notFound())

        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.registra(RegistraChaveRequest.newBuilder()
                .setClienteId(CLIENTE_INEXISTENTE.toString())
                .setTipoChave(TipoChave.EMAIL)
                .setValorChave("rponte@gmail.com")
                .setTipoConta(TipoConta.CONTA_CORRENTE)
                .build())
        }

        with(excecao) {
            assertEquals(Status.FAILED_PRECONDITION.code, status.code)
            assertEquals("Cliente não encontrado no Itau", status.description)
        }
    }

    @Test
    fun `nao deve registrar chave pix quando parametros forem invalidos` () {
        val excecao = assertThrows<StatusRuntimeException> {
            grpcClient.registra(RegistraChaveRequest.newBuilder().build())
        }

        with(excecao) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("dados de entrada invalidos", status.description)
        }
    }
}

@MockBean(ContasDeClientesNoItau::class)
fun itauClient(): ContasDeClientesNoItau? {
    return Mockito.mock(ContasDeClientesNoItau::class.java)
}

@Factory
class Clients {
    @Bean
    fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
            RegistraChaveGrpcServiceGrpc.RegistraChaveGrpcServiceBlockingStub{
        return RegistraChaveGrpcServiceGrpc.newBlockingStub(channel)
    }
}

private fun dadosDaContaResponse(): DadosDaContaResponse {

    return DadosDaContaResponse(
        tipo = "CONTA_CORRENTE",
        instituicao = InstituicaoResponse(
            nome = "ITAÚ UNIBANCO S.A.",
            ispb = "60701190"
        ),
        agencia = "0001",
        numero = "291900",
        titular = TitularResponse(
            nome = "Rafael M C Ponte",
            cpf = "02467781054"
        )
    )
}


