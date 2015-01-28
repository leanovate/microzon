node /^consul\d+\..*$/ {
	include "common"
	include "supervisor"
    include "consul"
}

node log {
    include "common"
    include "java8"
    include "elasticsearch"
    include "kibana"
    include "logstash"
    include "logstash_forwarder"
    include "supervisor"

    Class["common"] -> Class["java8"] -> Class["logstash_forwarder"] -> Class["elasticsearch"] -> Class["kibana"] -> Class["logstash"]
}

