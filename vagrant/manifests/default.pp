node log {
    include "java"
    include "elasticsearch"
    include "kibana"
    include "logstash"
    include "logstash_forwarder"

    Class["java"] -> Class["logstash_forwarder"] -> Class["elasticsearch"] -> Class["kibana"] -> Class["logstash"]
}

node web {
    include "java"
    include "logstash_forwarder"

    Class["java"] -> Class["logstash_forwarder"]
}