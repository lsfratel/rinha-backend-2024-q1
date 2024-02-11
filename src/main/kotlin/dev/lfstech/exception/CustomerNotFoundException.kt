package dev.lfstech.exception

import io.ktor.http.*

class CustomerNotFoundException(
  message: String? = null,
  cause: Throwable? = null,
  val statusCode: HttpStatusCode = HttpStatusCode.NotFound,
) : Exception(message, cause)
