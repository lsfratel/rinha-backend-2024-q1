package dev.lfstech.rounting

import dev.lfstech.exception.CustomerNotFoundException
import dev.lfstech.exception.InvalidPathParamTypeException
import dev.lfstech.rounting.request.TransactionRequest
import dev.lfstech.rounting.response.StatementResponse
import dev.lfstech.service.CustomerService
import dev.lfstech.service.TransactionService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.customerRoute() {
  val customerService by inject<CustomerService>()
  val transactionService by inject<TransactionService>()

  get("/{id}/extrato") {
    val id = call.parameters["id"]
      ?.toIntOrNull() ?: throw InvalidPathParamTypeException()

    val customer = customerService.getById(id)
      ?: throw CustomerNotFoundException("Cliente não encontrado")

    val transactions = transactionService.getLastTransactionsByUserId(id)

    call.respond(
      message = StatementResponse.from(customer, transactions)
    )
  }

  post("/{id}/transacoes") {
    val id = call.parameters["id"]
      ?.toIntOrNull() ?: throw InvalidPathParamTypeException()

    val body = call.receive<TransactionRequest>()

    val result = transactionService.transact(id, body)

    call.respond(
      message = result
    )
  }
}
