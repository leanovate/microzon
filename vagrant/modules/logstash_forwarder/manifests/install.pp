class logstash_forwarder::install {
    package { "logstash-forwarder":
        ensure => "0.3.1",
    }
    
}