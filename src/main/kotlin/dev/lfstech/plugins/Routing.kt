package dev.lfstech.plugins

import dev.lfstech.rounting.customerRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        route("/clientes/{id}") {
            customerRoute()
        }
    }

}