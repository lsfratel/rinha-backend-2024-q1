package dev.lfstech.repository

import dev.lfstech.data.Transaction
import dev.lfstech.data.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.komapper.core.dsl.*
import org.komapper.core.dsl.operator.desc
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.jdbc.JdbcDatabase
import java.time.LocalDateTime

class TransactionRepository : KoinComponent {
  private val db by inject<JdbcDatabase>()
  private val tr = Meta.transaction

  fun lastTransactionsById(id: Int, limit: Int) = db.runQuery {
    QueryDsl
      .from(tr)
      .where { tr.customerId eq id }
      .limit(limit)
      .orderBy(tr.createdAt.desc())
  }

  fun create(customerId: Int, amout: Long, type: String, description: String) = db.runQuery {
    QueryDsl
      .insert(tr)
      .single(
        Transaction(
          id = 0,
          customerId = customerId,
          amount = amout,
          type = type,
          description = description,
          createdAt = LocalDateTime.now()
        )
      )
  }

  fun acquireLock(id: Int, block: () -> Map<String, Long>) = db.withTransaction {
    db.runQuery {
      QueryDsl
        .executeScript("select pg_advisory_xact_lock(${id})")
    }

    block()
  }
}