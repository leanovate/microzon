class app_cart {
    contain "app_cart::install"
    contain "app_cart::config"
    contain "app_cart::service"

    Class["app_cart::install"] -> Class["app_cart::config"] ~> Class["app_cart::service"]
    Class["app_cart::install"] ~> Class["app_cart::service"]
}