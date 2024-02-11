package dev.lfstech.service

import dev.lfstech.data.Customer
import dev.lfstech.repository.CustomerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CustomerService : KoinComponent {
  private val repository by inject<CustomerRepository>()

  fun getById(id: Int) = repository
    .getById(id)

  fun update(customer: Customer) = repository
    .update(customer)
}
