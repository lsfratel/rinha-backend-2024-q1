package dev.lfstech.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    val user = environment.config.property("ktor.db.pgUser").getString()
    val pass = environment.config.property("ktor.db.pgPassword").getString()
    val database = environment.config.property("ktor.db.pgDatabase").getString()
    val port = environment.config.property("ktor.db.pgPort").getString().toInt()
    val server = environment.config.property("ktor.db.pgServer").getString()

    val hkConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://$server:$port/$database"
        username = user
        password = pass
        addDataSourceProperty("cacheServerConfiguration", true)
        addDataSourceProperty("cachePrepStmts", "true")
        addDataSourceProperty("useUnbufferedInput", "false")
        addDataSourceProperty("prepStmtCacheSize", "4096")
        addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
        maximumPoolSize = 4
        minimumIdle = 4
        validate()
    }

    Database.connect(HikariDataSource(hkConfig))
}
