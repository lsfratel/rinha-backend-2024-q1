package dev.lfstech

import dev.lfstech.plugin.configureAll
import dev.lfstech.rounting.customerRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun main(args: Array<String>) =
  io.ktor.server.cio.EngineMain.main(args)

fun Application.module() {
  configureAll()

  routing {
    route("/clientes") {
      customerRoute()
    }
  }
}
