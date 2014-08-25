package modules

import scaldi.Module
import backend.{CartBackend, ProductBackend, CustomerBackend}
import logging.CorrelatedWS

class BackendModule extends Module {
  binding to new CorrelatedWS
  binding to new CustomerBackend
  binding to new ProductBackend
  binding to new CartBackend
}
