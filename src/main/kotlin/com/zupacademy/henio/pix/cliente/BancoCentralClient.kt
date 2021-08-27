package com.zupacademy.henio.pix.cliente

import com.zupacademy.henio.pix.chave.ChavePixEntity
import com.zupacademy.henio.pix.chave.ContaAssociada
import com.zupacademy.henio.pix.chave.TipoDeChave
import com.zupacademy.henio.pix.chave.TipoDeConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime

@Client("\${bc.pix.url}")
interface BancoCentralClient {

    @Post(value = "/api/v1/pix/keys",
        produces = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML])
    fun cadastraChaveNoBC(@Body request: CreatePixKeyRequest) : HttpResponse<CreatePixKeyResponse>

    @Delete(value = "/api/v1/pix/keys/{key}",
        produces = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML])
    fun excluiChaveNoBC(@PathVariable key: String, @Body request: DeletePixKeyRequest)
            : HttpResponse<DeletePixKeyResponse>

    @Get(value = "/api/v1/pix/keys/{key}",
        consumes = [MediaType.APPLICATION_XML])
    fun consultaPorChave(@PathVariable key: String): HttpResponse<PixKeyDetailsResponse>
}

data class DeletePixKeyRequest(
    val key: String,
    val participant: String = "60701190",
)

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
)

data class CreatePixKeyRequest(
    val keyType: PixKeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
) {
    companion object {

        fun of(chave: ChavePixEntity): CreatePixKeyRequest {
            return CreatePixKeyRequest(
                keyType = PixKeyType.by(chave.tipoChave),
                key = chave.chave,
                bankAccount = BankAccount (
                    participant =  "60701190",
                    branch = chave.conta.agencia,
                    accountNumber = chave.conta.numero,
                    accountType =  BankAccount.AccountType.by(chave.tipoConta),
                ),
                owner = Owner(
                    type = Owner.OwnerType.NATURAL_PERSON,
                    name = chave.conta.nomeDoTitular,
                    taxIdNumber = chave.conta.cpfDoTitular
                )
            )
        }
    }
}

data class PixKeyDetailsResponse(
    val keyType: PixKeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {

    fun toModel(): ChavePixInfo {
        return ChavePixInfo(
            tipoChave = keyType.domainType!!,
            chave = this.key,
            tipoConta = when (this.bankAccount.accountType) {
                BankAccount.AccountType.CACC -> TipoDeConta.CONTA_CORRENTE
                BankAccount.AccountType.SVGS -> TipoDeConta.CONTA_POUPANCA
            },
            conta = ContaAssociada(
                instituicao = bankAccount.participant,
                nomeDoTitular = owner.name,
                cpfDoTitular = owner.taxIdNumber,
                agencia = bankAccount.branch,
                numero = bankAccount.accountNumber
            )
        )
    }
}

data class ChavePixInfo (
    val tipoChave: TipoDeChave,
    var chave: String,
    val tipoConta: TipoDeConta,
    val conta: ContaAssociada,

    )

data class CreatePixKeyResponse (
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
)

data class Owner (
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
) {
    enum class OwnerType {
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}


data class BankAccount (
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
) {
    enum class AccountType {

        CACC,
        SVGS;

        companion object {
            fun by(domainType: TipoDeConta): AccountType {
                return when (domainType) {
                    TipoDeConta.CONTA_CORRENTE -> CACC
                    TipoDeConta.CONTA_POUPANCA -> SVGS
                }
            }
        }
    }
}

enum class PixKeyType(val domainType: TipoDeChave?) {

    CPF(TipoDeChave.CPF),
    CNPJ(null),
    PHONE(TipoDeChave.PHONE),
    EMAIL(TipoDeChave.EMAIL),
    RANDOM(TipoDeChave.RANDOM);

    companion object {
        private val mapping = PixKeyType.values().associateBy(PixKeyType::domainType)

        fun by(domainType: TipoDeChave): PixKeyType {
            return mapping[domainType] ?: throw IllegalArgumentException("PixKeyType invalida")
        }
    }
}