package modules

import scaldi.Module
import backend.{ProductBackend, CustomerBackend}

class BackendModule extends Module {
  binding to new CustomerBackend
  binding to new ProductBackend
}
