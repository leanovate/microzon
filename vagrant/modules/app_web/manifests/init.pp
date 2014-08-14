class app_web {
    include "app_web::install"
    include "app_web::config"
    include "app_web::service"

    Class["app_web::install"] -> Class["app_web::config"] ~> Class["app_web::service"]
}