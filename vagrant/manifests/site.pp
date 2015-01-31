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
    include "consul_agent"
    include "zipkin_collector"
    include "supervisor"

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

node /^mongo\..*$/ {
	include "common"
    include "logstash_forwarder"
	include "consul_agent"
	include "supervisor"
	include "app_mongo"
}

node /^customer\d+\..*$/ {
	include "common"
	include "java8"
    include "logstash_forwarder"
	include "consul_agent"
	include "app_customer"
	include "supervisor"
}

node /^product\d+\..*$/ {
	include "common"
	include "java8"
    include "logstash_forwarder"
	include "consul_agent"
	include "supervisor"	
}

node /^cart\d+\..*$/ {
	include "common"
	include "java8"
    include "logstash_forwarder"
	include "consul_agent"
	include "app_cart"
	include "supervisor"	
}