node /^consul\d+\..*$/ {
	include "common"
	include "supervisor"
    include "consul"
}

node /^log\..*$/ {
    include "common"
    include "java8"
    include "elasticsearch"
    include "kibana"
    include "logstash"
    include "logstash_forwarder"
    include "consul_agent"
    include "supervisor"

    Class["common"] -> Class["java8"] -> Class["logstash_forwarder"] -> 
    Class["elasticsearch"] -> Class["kibana"] -> Class["logstash"] 
}

node /^zipkin\..*$/ {
    include "common"
    include "java"
    include "zipkin_collector"

    Class["common"] -> Class["java"] -> Class["zipkin_collector"]
}

node /^mysql\..*$/ {
	include "common"
    include "logstash_forwarder"
    include "mysql"
	include "consul_agent"
	include "supervisor"
    include "app_mysql"
}