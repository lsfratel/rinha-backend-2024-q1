package dev.lfstech.plugin

import dev.lfstech.rounting.request.TransactionRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
  install(RequestValidation) {
    validate<TransactionRequest> { tr ->
      when {
        tr.descricao.isBlank() -> ValidationResult.Invalid("Descrição é obrigatória")
        tr.descricao.length > 10 -> ValidationResult.Invalid("Descrição deve conter até 10 caracteres")
        tr.tipo !in listOf("c", "d") -> ValidationResult.Invalid("O tipo deve ser c ou d")
        else -> ValidationResult.Valid
      }
    }
  }
}
