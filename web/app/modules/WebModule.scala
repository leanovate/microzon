package modules

import scaldi.Module
import controllers.{UserRegistrationController, LoginController}

class WebModule extends Module {
  binding to new LoginController
  binding to new UserRegistrationController
}
