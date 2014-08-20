
class app_product {
    contain "app_product::install"
    contain "app_product::config"
    contain "app_product::service"

    Class["app_product::install"] -> Class["app_product::config"] ~> Class["app_product::service"]
    Class["app_product::install"] ~> Class["app_product::service"]
}
