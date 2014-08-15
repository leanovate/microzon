class app_product::install {
    exec { "apt-key mongo db":
        command => "/usr/bin/apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10"
    }

    file { "/etc/apt/sources.list.d/mongodb.list":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/app_product/mongodb.list",
    }


    exec { "apt update /etc/apt/sources.list.d/mongodb.list":
        command => "/usr/bin/apt-get update",
        refreshonly => true,
        subscribe   => File["/etc/apt/sources.list.d/mongodb.list"],
        require     => Exec["apt-key mongo db"]
    }

    package { ["mongodb-org",  "mongodb-org-server", "mongodb-org-shell", "mongodb-org-mongos", "mongodb-org-tools"]:
        ensure => "2.6.1",
        require => Exec["apt update /etc/apt/sources.list.d/mongodb.list"]
    }
}
