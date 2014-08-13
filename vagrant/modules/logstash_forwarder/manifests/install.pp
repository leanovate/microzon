class logstash_forwarder::install {
    file { "/etc/apt/rkeys":
        owner => root,
        group => root,
        mode => 700,
        ensure => directory,
    }

    file { "/etc/apt/rkeys/GPG-KEY-elasticsearch":
        owner => root,
        group => root,
        mode => 600,
        ensure => file,
        source => "puppet:///modules/logstash_forwarder/GPG-KEY-elasticsearch",
    }

    exec { "apt-key /etc/apt/rkeys/GPG-KEY-elasticsearch":
        command => "/usr/bin/apt-key add /etc/apt/rkeys/GPG-KEY-elasticsearch",
        refreshonly => true,
        subscribe   => File["/etc/apt/rkeys/GPG-KEY-elasticsearch"],
    }

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
        subscribe   => [File["/etc/apt/sources.list.d/logstashforwarder.list"],Exec["apt-key /etc/apt/rkeys/GPG-KEY-elasticsearch"]]
    }

    package { "logstash-forwarder":
        ensure => "0.3.1",
        require => [Exec["apt update /etc/apt/sources.list.d/logstashforwarder.list"], Exec["apt-key /etc/apt/rkeys/GPG-KEY-elasticsearch"]]
    }
    
}