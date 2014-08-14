class app_customer {
    include "app_customer::install"
    include "app_customer::config"
    include "app_customer::service"

    Class["app_customer::install"] -> Class["app_customer::config"] ~> Class["app_customer::service"]
}