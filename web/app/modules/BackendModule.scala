package modules

import scaldi.Module
import backend.CustomerBackend

class BackendModule extends Module {
  binding to new CustomerBackend
}
