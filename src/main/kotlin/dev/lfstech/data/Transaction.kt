package dev.lfstech.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Transactions : IntIdTable("transactions") {
    val customer = reference("customer_id", Customers)
    val type = char("type")
    val amount = long("amount")
    val description = varchar("description", 10)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}

class TransactionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TransactionEntity>(Transactions)

    var type by Transactions.type
    var amount by Transactions.amount
    var description by Transactions.description
    var createdAt by Transactions.createdAt
    var customer by CustomerEntity referencedOn Transactions.customer
}

