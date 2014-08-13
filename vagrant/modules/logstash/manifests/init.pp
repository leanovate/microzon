class logstash {
    include "logstash::install"
    include "logstash::config"
    include "logstash::service"

    Class["logstash::install"] -> Class["logstash::config"] ~> Class["logstash::service"]
}