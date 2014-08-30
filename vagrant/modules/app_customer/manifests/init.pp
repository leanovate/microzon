class app_customer(
    $dist_url
) {
    contain "app_customer::install"
    contain "app_customer::config"
    contain "app_customer::service"

    Class["app_customer::install"] -> Class["app_customer::config"] ~> Class["app_customer::service"]
    Class["app_customer::install"] ~> Class["app_customer::service"]
}