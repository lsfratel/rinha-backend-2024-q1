package dev.lfstech.rounting.response

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val limite: Long,
    val saldo: Long
)
