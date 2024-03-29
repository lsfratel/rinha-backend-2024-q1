package dev.lfstech.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.komapper.dialect.postgresql.jdbc.PostgreSqlJdbcDialect
import org.komapper.jdbc.JdbcDatabase

fun configureDatabase(env: ApplicationEnvironment): JdbcDatabase {
  val c = env.config

  val db = c.property("db.database").getString()
  val port = c.property("db.port").getString()
  val server = c.property("db.server").getString()

  val dataSource = HikariDataSource(HikariConfig().apply {
    jdbcUrl = "jdbc:postgresql://$server:$port/$db"
    username = c.property("db.user").getString()
    password = c.property("db.password").getString()
    maximumPoolSize = 2
    minimumIdle = 2
    isAutoCommit = false
    connectionTimeout = 8000
    validate()
  })

  val database = JdbcDatabase(dataSource, PostgreSqlJdbcDialect())

  return database
}
