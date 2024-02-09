package dev.lfstech.rounting.response

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponse(
    val code: Int,
    val message: String
)
