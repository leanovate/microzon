class elasticsearch::install {
    file { "/etc/apt/sources.list.d/elasticsearch.list":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/elasticsearch/elasticsearch.list",
    }

    exec { "apt update /etc/apt/sources.list.d/elasticsearch.list":
        command => "/usr/bin/apt-get update",
        refreshonly => true,
        subscribe   => File["/etc/apt/sources.list.d/elasticsearch.list"],
    }

    package { "elasticsearch":
        ensure => "1.1.1",
        require => Exec["apt update /etc/apt/sources.list.d/elasticsearch.list"]
    }
}