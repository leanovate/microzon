class logstash::install {
    file { "/etc/apt/sources.list.d/logstash.list":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/logstash/logstash.list",
    }

    exec { "apt update /etc/apt/sources.list.d/logstash.list":
        command => "/usr/bin/apt-get update",
        refreshonly => true,
        subscribe   => File["/etc/apt/sources.list.d/logstash.list"],
    }

    package { "logstash":
        ensure => "1.4.2-1-2c0f5a1",
        require => Exec["apt update /etc/apt/sources.list.d/logstash.list"]
    }
}