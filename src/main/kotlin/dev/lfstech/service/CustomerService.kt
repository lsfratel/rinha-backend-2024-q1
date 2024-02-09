package dev.lfstech.service

import dev.lfstech.data.CustomerEntity
import dev.lfstech.rounting.request.TransactionRequest
import dev.lfstech.util.UnprocessableEntity
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.transactions.transaction

class CustomerService(
    private val transactionService: TransactionService
) {
    fun getById(id: Int) = transaction {
        CustomerEntity
            .findById(id)
    }

    fun transact(id: Int, req: TransactionRequest) = transaction {
        exec("select pg_advisory_xact_lock($id)")
        val customer = getById(id)!!

        if (req.tipo.toString() == "d" && (customer.balance - req.valor < -customer.credit))
            throw UnprocessableEntity("Crédito insuficiente")

        when (req.tipo.toString()) {
            "d" -> customer.balance -= req.valor
            "c" -> customer.balance += req.valor
        }

        transactionService.create(customer, req)

        customer
    }
}
