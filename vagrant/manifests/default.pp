node log {
    include "java"
    include "elasticsearch"
    include "kibana"
    include "logstash"
    include "logstash_forwarder"

    Class["java"] -> Class["elasticsearch"] -> Class["kibana"] -> Class["logstash"] -> Class["logstash_forwarder"]
}