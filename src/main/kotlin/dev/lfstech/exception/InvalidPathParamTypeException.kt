package dev.lfstech.exception

import io.ktor.http.*

class InvalidPathParamTypeException(
  message: String? = null,
  cause: Throwable? = null,
  val statusCode: HttpStatusCode = HttpStatusCode.NotFound,
) : Exception(null, cause)
