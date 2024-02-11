package dev.lfstech.plugin

import io.ktor.server.application.*

fun Application.configureAll() {
  configureSerialization()
  configureStatusPage()
  configureRequestValidation()
  configureKoin()
}
