class logstash_forwarder::install {
    file { "/etc/apt/sources.list.d/logstashforwarder.list":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/logstash_forwarder/logstashforwarder.list",
    }

    exec { "apt update /etc/apt/sources.list.d/logstashforwarder.list":
        command => "/usr/bin/apt-get update",
        refreshonly => true,
        subscribe   => File["/etc/apt/sources.list.d/logstashforwarder.list"],
    }

    package { "logstash-forwarder":
        ensure => present,
        require => Exec["apt update /etc/apt/sources.list.d/logstashforwarder.list"]
    }
    
}