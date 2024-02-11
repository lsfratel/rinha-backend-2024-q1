package dev.lfstech.data

import kotlinx.serialization.Serializable
import org.komapper.annotation.*

@Serializable
data class Customer(
  val id: Int,
  val name: String,
  val credit: Long,
  var balance: Long
)

@KomapperEntityDef(Customer::class)
@KomapperTable("customers")
data class CustomerDef(
  @KomapperId
  @KomapperAutoIncrement
  val id: Nothing,
  val name: Nothing,
  val credit: Nothing,
  val balance: Nothing
)
