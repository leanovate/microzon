class zipkin_collector {
    contain "zipkin_collector::install"
    contain "zipkin_collector::config"
    contain "zipkin_collector::service"

    Class["zipkin_collector::install"] -> Class["zipkin_collector::config"] ~> Class["zipkin_collector::service"]
}