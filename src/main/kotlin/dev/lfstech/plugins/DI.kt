package dev.lfstech.plugins

import dev.lfstech.service.CustomerService
import dev.lfstech.service.TransactionService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.MainBuilder.bindServices() {
    bind<TransactionService>() with singleton { TransactionService() }
    bind<CustomerService>() with singleton { CustomerService(instance()) }
}
