class logstash {
    contain "logstash::install"
    contain "logstash::config"
    contain "logstash::service"

    Class["logstash::install"] -> Class["logstash::config"] ~> Class["logstash::service"]
}