package dev.lfstech.plugin

import dev.lfstech.config.configureDatabase
import dev.lfstech.repository.CustomerRepository
import dev.lfstech.repository.TransactionRepository
import dev.lfstech.service.CustomerService
import dev.lfstech.service.TransactionService
import io.ktor.server.application.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.komapper.jdbc.JdbcDatabase

fun default(env: ApplicationEnvironment) = module {
  single<CustomerRepository> { CustomerRepository() }
  single<TransactionRepository> { TransactionRepository() }
  single<CustomerService> { CustomerService() }
  single<TransactionService> { TransactionService() }
  single<JdbcDatabase> { configureDatabase(env) }
}

fun Application.configureKoin() {
  startKoin {
    modules(default(environment))
  }
}