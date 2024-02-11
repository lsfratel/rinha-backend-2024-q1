package dev.lfstech.plugin

import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase

fun Application.configureAll() {
  configureSerialization()
  configureStatusPage()
  configureRequestValidation()
  configureKoin()

  environment.monitor.subscribe(ApplicationStarted) { _ ->
    val db by inject<JdbcDatabase>()
    db.runQuery {
      QueryDsl
        .executeScript("SELECT 1")
    }
  }

  environment.monitor.subscribe(ApplicationStopped) { app ->
    app.environment.monitor.unsubscribe(ApplicationStarted) {}
  }
}
