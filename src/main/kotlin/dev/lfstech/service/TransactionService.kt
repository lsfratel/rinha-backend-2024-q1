package dev.lfstech.service

import dev.lfstech.exception.CustomerNotFoundException
import dev.lfstech.exception.InsufficientCreditException
import dev.lfstech.repository.TransactionRepository
import dev.lfstech.rounting.request.TransactionRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TransactionService : KoinComponent {
  private val repository by inject<TransactionRepository>()
  private val customerService by inject<CustomerService>()

  private fun create(clientId: Int, bodyRequest: TransactionRequest) = repository
    .create(clientId, bodyRequest.valor, bodyRequest.tipo, bodyRequest.descricao)

  fun getLastTransactionsByUserId(id: Int) = repository
    .lastTransactionsById(id, 10)

  fun transact(customerId: Int, bodyRequest: TransactionRequest) = repository.acquireLock(customerId) {
    val customer = customerService.getById(customerId)
      ?: throw CustomerNotFoundException("Cliente não encontrado")

    if (bodyRequest.tipo == "d" && (customer.balance - bodyRequest.valor < -customer.credit)) {
      throw InsufficientCreditException("Crédito insuficiente")
    }

    when (bodyRequest.tipo) {
      "d" -> customer.balance -= bodyRequest.valor
      "c" -> customer.balance += bodyRequest.valor
    }

    customerService.update(customer)
    create(customer.id, bodyRequest)

    return@acquireLock mapOf(
      "limite" to customer.credit,
      "saldo" to customer.balance
    )
  }
}