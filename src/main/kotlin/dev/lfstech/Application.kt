package dev.lfstech

import dev.lfstech.plugins.*
import io.ktor.server.application.*
import org.kodein.di.ktor.di

fun main(args: Array<String>) =
    io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureStatusPage()
    configureRequestValidation()
    configureRouting()

    di {
        bindServices()
    }
}
