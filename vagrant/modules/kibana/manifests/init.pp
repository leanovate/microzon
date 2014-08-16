class kibana {
    contain "kibana::install"
    contain "kibana::config"
    contain "kibana::service"

    Class["kibana::install"] -> Class["kibana::config"] ~> Class["kibana::service"]
}