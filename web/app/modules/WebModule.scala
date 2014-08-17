package modules

import scaldi.Module
import controllers.{ApplicationController, ShopController, UserRegistrationController, UserLoginController}

class WebModule extends Module {
  binding to new UserLoginController
  binding to new UserRegistrationController
  binding to new ShopController
  binding to new ApplicationController
}
