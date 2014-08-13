class logstash::service {
    service { "logstash":
        enable => true,
        ensure => running,
        hasstatus => true,
        hasrestart => true
    }
}