class app_web(
    $dist_url
) {
    contain "app_web::install"
    contain "app_web::config"
    contain "app_web::service"

    Class["app_web::install"] -> Class["app_web::config"] ~> Class["app_web::service"]
    Class["app_web::install"] ~> Class["app_web::service"]
}