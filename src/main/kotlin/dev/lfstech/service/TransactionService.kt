package dev.lfstech.service

import dev.lfstech.data.*
import dev.lfstech.rounting.request.TransactionRequest
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction

class TransactionService {
    fun create(cus: CustomerEntity, req: TransactionRequest) = transaction {
        TransactionEntity.new {
            amount = req.valor
            type = req.tipo
            description = req.descricao
            customer = cus
        }
    }

    fun getByClient(clientId: Int) = transaction {
        TransactionEntity
            .find {
                Transactions.customer eq clientId
            }
            .limit(10)
            .orderBy(Transactions.createdAt to SortOrder.DESC)
            .toList()
    }
}