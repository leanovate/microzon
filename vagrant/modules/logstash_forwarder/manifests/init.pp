class logstash_forwarder(
	$logstash_host
) {
    contain "logstash_forwarder::install"
    contain "logstash_forwarder::config"
    contain "logstash_forwarder::service"

    Class["logstash_forwarder::install"] -> Class["logstash_forwarder::config"] ~> Class["logstash_forwarder::service"]
}