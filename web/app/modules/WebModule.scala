package modules

import scaldi.Module
import controllers.{ShopController, UserRegistrationController, UserLoginController}

class WebModule extends Module {
  binding to new UserLoginController
  binding to new UserRegistrationController
  binding to new ShopController
}
