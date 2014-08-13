class logstash_forwarder::service {
    service { "logstash-forwarder":
        enable => true,
        ensure => running,
        hasstatus => true,
        hasrestart => true
    }    
}