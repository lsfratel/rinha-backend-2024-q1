package dev.lfstech.data

import dev.lfstech.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.komapper.annotation.*
import java.time.LocalDateTime

@Serializable
data class Transaction(
  val id: Int,
  val customerId: Int,
  val type: String,
  val amount: Long,
  val description: String,
  @Serializable(with = LocalDateTimeSerializer::class)
  val createdAt: LocalDateTime = LocalDateTime.now()
)

@KomapperEntityDef(Transaction::class)
@KomapperTable("transactions")
data class TransactionDef(
  @KomapperId
  @KomapperAutoIncrement
  val id: Nothing,
  @KomapperColumn("customer_id")
  val customerId: Nothing,
  @KomapperColumn("type")
  val type: Nothing,
  val amount: Nothing,
  val description: Nothing,
  @KomapperCreatedAt
  val createdAt: Nothing
)