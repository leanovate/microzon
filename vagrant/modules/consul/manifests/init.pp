class consul {
    contain "consul::install"
    contain "consul::config"
    contain "consul::service"

    Class["consul::install"] ->
    Class["consul::config"] ~>
    Class["consul::service"]
}