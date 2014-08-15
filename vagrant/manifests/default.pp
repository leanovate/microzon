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
    include "app_web"

    Class["java"] -> Class["logstash_forwarder"] -> Class["app_web"]
}

node customer {
    include "java"
    include "logstash_forwarder"
    include "mysql"
    include "app_customer"

    Class["java"] -> Class["mysql"] -> Class["logstash_forwarder"] -> Class["app_customer"]
}

node product {
    include "java"
    include "logstash_forwarder"
    include "app_product"

    Class["java"] -> Class["logstash_forwarder"] -> Class["app_product"]
}