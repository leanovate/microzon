node log {
    include "common"
    include "java"
    include "elasticsearch"
    include "kibana"
    include "logstash"
    include "logstash_forwarder"

    Class["common"] -> Class["java"] -> Class["logstash_forwarder"] -> Class["elasticsearch"] -> Class["kibana"] -> Class["logstash"]
}

node web {
    include "common"
    include "java"
    include "logstash_forwarder"
    include "app_web"

    Class["common"] -> Class["java"] -> Class["logstash_forwarder"] -> Class["app_web"]
}

node customer {
    include "common"
    include "java"
    include "logstash_forwarder"
    include "mysql"
    include "app_customer"

    Class["common"] -> Class["java"] -> Class["mysql"] -> Class["logstash_forwarder"] -> Class["app_customer"]
}

node product {
    include "common"
    include "java"
    include "logstash_forwarder"
    include "app_product"

    Class["common"] -> Class["java"] -> Class["logstash_forwarder"] -> Class["app_product"]
}

node zipkin {
    include "common"
    include "java"
    include "zipkin_collector"

    Class["common"] -> Class["java"] -> Class["zipkin_collector"]
}