class app_billing {
    contain "app_billing::install"
    contain "app_billing::config"
    contain "app_billing::service"

    Class["app_billing::install"] -> Class["app_billing::config"] ~> Class["app_billing::service"]
    Class["app_billing::install"] ~> Class["app_billing::service"]
}