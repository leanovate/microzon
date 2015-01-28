class logstash::config {    
    file { ["/etc/pki/server"]:
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file        
    }

    file { "/etc/pki/server/logstash-forwarder.crt":
        source => "puppet:///modules/logstash/logstash-forwarder.crt"
    }

    file { "/etc/pki/server/logstash-forwarder.key":
        source => "puppet:///modules/logstash/logstash-forwarder.key"
    }

    file { "/etc/logstash/conf.d/01-lumberjack-input.conf":
        source => "puppet:///modules/logstash/01-lumberjack-input.conf"
    }

    file { "/etc/logstash/conf.d/10-syslog.conf":
        source => "puppet:///modules/logstash/10-syslog.conf"
    }

    file { "/etc/logstash/conf.d/11-application.conf":
        source => "puppet:///modules/logstash/11-application.conf"
    }

    file { "/etc/logstash/conf.d/12-accesslog.conf":
        source => "puppet:///modules/logstash/12-accesslog.conf"
    }

    file { "/etc/logstash/conf.d/30-lumberjack-output.conf":
        source => "puppet:///modules/logstash/30-lumberjack-output.conf"
    }

    file { "/etc/consul.d/logstash.json":
        source => "puppet:///modules/logstash/logstash.json"
    }
}