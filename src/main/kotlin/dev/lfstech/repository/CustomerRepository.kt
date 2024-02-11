package dev.lfstech.repository

import dev.lfstech.data.Customer
import dev.lfstech.data.customer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.komapper.core.dsl.*
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.jdbc.JdbcDatabase

class CustomerRepository : KoinComponent {
  private val db by inject<JdbcDatabase>()
  private val cu = Meta.customer

  fun getById(id: Int) = db.runQuery {
    QueryDsl
      .from(cu)
      .where { cu.id eq id }
      .singleOrNull()
  }

  fun update(customer: Customer) = db.runQuery {
    QueryDsl
      .update(cu)
      .single(customer)
  }
}
