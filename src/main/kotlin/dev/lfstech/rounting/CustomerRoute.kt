package dev.lfstech.rounting

import dev.lfstech.data.CustomerEntity
import dev.lfstech.data.TransactionEntity
import dev.lfstech.rounting.request.TransactionRequest
import dev.lfstech.rounting.response.Balance
import dev.lfstech.rounting.response.StatementResponse
import dev.lfstech.rounting.response.Transaction
import dev.lfstech.rounting.response.TransactionResponse
import dev.lfstech.service.CustomerService
import dev.lfstech.service.TransactionService
import dev.lfstech.util.UnprocessableEntity
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.security.InvalidParameterException
import java.time.LocalDateTime

fun Route.customerRoute() {
    val customerService by closestDI().instance<CustomerService>()
    val transactionService by closestDI().instance<TransactionService>()

    post("/transacoes") {
        val id = call.parameters["id"]
            ?.toIntOrNull() ?: throw InvalidParameterException("Id deve ser um inteiro")

        customerService.getById(id)
            ?: throw NotFoundException("Cliente não encontrado")

        var req: TransactionRequest

        try {
            req = call.receive<TransactionRequest>()
        } catch (e: Throwable) {
            throw UnprocessableEntity("Body incorreto")
        }

        val customer = customerService.transact(id, req)

        call.respond(
            message = customer.toTransactionResponse()
        )
    }

    get("/extrato") {
        val id = call.parameters["id"]
            ?.toIntOrNull() ?: throw InvalidParameterException("Id deve ser um inteiro")

        val customer = customerService.getById(id)
            ?: throw NotFoundException("Cliente não encontrado")

        val customerTransactions = transactionService.getByClient(customer.id.value)

        call.respond(
            message = customer.toStatementResponse(customerTransactions)
        )
    }
}

private fun CustomerEntity.toTransactionResponse() =
    TransactionResponse(
        saldo = balance,
        limite = credit
    )

private fun TransactionEntity.toStatementResponse() =
    Transaction(
        tipo = type,
        valor = amount,
        descricao = description,
        realizado_em = createdAt
    )

private fun CustomerEntity.toStatementResponse(transactions: List<TransactionEntity>) =
    StatementResponse(
        saldo = Balance(
            total = balance,
            limite = credit,
            data_extrato = LocalDateTime.now()
        ),
        ultimas_transacoes = transactions
            .map { it.toStatementResponse() }
    )