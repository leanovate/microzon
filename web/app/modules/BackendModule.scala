package modules

import scaldi.Module
import backend._
import logging.CorrelatedWS

class BackendModule extends Module {
  binding to new CorrelatedWS
  binding to new ServiceFailover
  binding to new ConsulLookup
  binding to new CustomerBackend
  binding to new ProductBackend
  binding to new CartBackend
  binding to new BillingBackend
}
