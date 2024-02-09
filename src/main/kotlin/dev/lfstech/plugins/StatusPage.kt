package dev.lfstech.plugins

import dev.lfstech.rounting.response.ExceptionResponse
import dev.lfstech.util.UnprocessableEntity
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.serialization.SerializationException
import java.security.InvalidParameterException

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            when (cause) {
                is UnprocessableEntity -> call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    ExceptionResponse(
                        message = cause.message ?: cause.toString(),
                        code = HttpStatusCode.UnprocessableEntity.value
                    )
                )

                is InvalidParameterException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse(
                        message = cause.message ?: cause.toString(),
                        code = HttpStatusCode.BadRequest.value
                    )
                )

                is NotFoundException -> call.respond(
                    HttpStatusCode.NotFound,
                    ExceptionResponse(
                        message = cause.message ?: cause.toString(),
                        code = HttpStatusCode.NotFound.value
                    )
                )

                is RequestValidationException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse(
                        message = cause.reasons.joinToString(),
                        code = HttpStatusCode.BadRequest.value
                    )
                )

                is BadRequestException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse(
                        message = cause.message ?: cause.toString(),
                        code = HttpStatusCode.BadRequest.value
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