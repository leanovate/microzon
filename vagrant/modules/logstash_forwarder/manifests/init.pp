class logstash_forwarder {
    include "logstash_forwarder::install"
    include "logstash_forwarder::config"
    include "logstash_forwarder::service"

    Class["logstash_forwarder::install"] -> Class["logstash_forwarder::config"] ~> Class["logstash_forwarder::service"]
}