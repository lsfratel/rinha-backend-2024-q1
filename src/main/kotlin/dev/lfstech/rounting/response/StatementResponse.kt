package dev.lfstech.rounting.response

import dev.lfstech.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Balance(
    val total: Long,
    val limite: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val data_extrato: LocalDateTime
)

@Serializable
data class Transaction(
    val tipo: Char,
    val valor: Long,
    val descricao: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val realizado_em: LocalDateTime
)

@Serializable
data class StatementResponse(
    val saldo: Balance,
    val ultimas_transacoes: List<Transaction>
)
