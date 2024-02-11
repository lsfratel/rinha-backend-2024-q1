package dev.lfstech.plugin

import dev.lfstech.exception.CustomerNotFoundException
import dev.lfstech.exception.InsufficientCreditException
import dev.lfstech.exception.InvalidPathParamTypeException
import dev.lfstech.rounting.response.ExceptionResponse
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPage() {
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      when (cause) {
        is InvalidPathParamTypeException -> call.respond(
          cause.statusCode,
        )

        is CustomerNotFoundException -> call.respond(
          status = cause.statusCode,
          message = cause.message.toString()
        )

        is BadRequestException -> call.respond(
          HttpStatusCode.UnprocessableEntity,
          ExceptionResponse(
            message = "Body inválido",
            code = HttpStatusCode.UnprocessableEntity.value
          )
        )

        is RequestValidationException -> call.respond(
          HttpStatusCode.UnprocessableEntity,
          ExceptionResponse(
            message = cause.reasons.joinToString(),
            code = HttpStatusCode.UnprocessableEntity.value
          )
        )

        is InsufficientCreditException -> call.respond(
          cause.statusCode,
          ExceptionResponse(
            message = cause.message.toString(),
            code = cause.statusCode.value
          )
        )

        else -> {
          cause.printStackTrace()
          call.respond(
            HttpStatusCode.InternalServerError,
            ExceptionResponse(
              message = "Internal error",
              code = HttpStatusCode.InternalServerError.value
            )
          )
        }
      }
    }
  }
}