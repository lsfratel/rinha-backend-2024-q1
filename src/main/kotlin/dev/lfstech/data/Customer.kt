package dev.lfstech.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Customers : IntIdTable("customers") {
    val name = varchar("name", 100)
    val credit = long("credit")
    val balance = long("balance")
}

class CustomerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CustomerEntity>(Customers)

    var name by Customers.name
    var credit by Customers.credit
    var balance by Customers.balance
    val transactions by TransactionEntity referrersOn Transactions.customer
}
