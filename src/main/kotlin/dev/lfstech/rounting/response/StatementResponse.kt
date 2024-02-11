package dev.lfstech.rounting.response

import dev.lfstech.data.Customer
import dev.lfstech.data.Transaction
import dev.lfstech.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class StatementBalance(
  val total: Long,
  val limite: Long,
  @Serializable(with = LocalDateTimeSerializer::class)
  val data_extrato: LocalDateTime
)

@Serializable
data class StatementTransaction(
  val tipo: String,
  val valor: Long,
  val descricao: String,
  @Serializable(with = LocalDateTimeSerializer::class)
  val realizado_em: LocalDateTime
)

@Serializable
data class StatementResponse(
  val saldo: StatementBalance,
  val ultimas_transacoes: List<StatementTransaction>
) {
  companion object {
    fun from(customer: Customer, transactions: List<Transaction>): StatementResponse {
      return StatementResponse(
        saldo = StatementBalance(
          total = customer.balance,
          limite = customer.credit,
          data_extrato = LocalDateTime.now()
        ),
        ultimas_transacoes = transactions.map {
          StatementTransaction(
            tipo = it.type,
            valor = it.amount,
            descricao = it.description,
            realizado_em = it.createdAt
          )
        }
      )
    }
  }
}

