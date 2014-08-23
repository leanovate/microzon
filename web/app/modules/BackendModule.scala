package modules

import scaldi.Module
import backend.{CartBackend, ProductBackend, CustomerBackend}

class BackendModule extends Module {
  binding to new CustomerBackend
  binding to new ProductBackend
  binding to new CartBackend
}
