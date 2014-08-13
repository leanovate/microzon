class logstash_forwarder::config {
    file { [ "/etc/pki/client"]:
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

    file { "/etc/pki/client/logstash-forwarder.crt":
        source => "puppet:///modules/logstash_forwarder/logstash-forwarder.crt"
    }

    file { "/etc/logstash-forwarder":
        source => "puppet:///modules/logstash_forwarder/logstash-forwarder"
    }
}