package dev.lfstech.exception

import io.ktor.http.*

class InsufficientCreditException(
  message: String? = null,
  cause: Throwable? = null,
  val statusCode: HttpStatusCode = HttpStatusCode.UnprocessableEntity,
) : Exception(message, cause)
