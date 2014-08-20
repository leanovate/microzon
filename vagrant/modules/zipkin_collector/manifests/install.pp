class zipkin_collector::install {
    package { "git":
        ensure => present
    }

    package { "supervisor":
        ensure => present
    }

    file { "/opt/app":
        ensure => directory,
        owner => root,
        group => root,
        mode => 755
    }

    exec { "git clone zipkin": 
        command => "/usr/bin/git clone https://github.com/twitter/zipkin.git",
        cwd => "/opt/app",
        creates => "/opt/app/zipkin",
        require => File["/opt/app"]
    }

    exec { "sbt publish-local":
        command => "/opt/app/zipkin/bin/sbt publish-local",
        cwd => "/opt/app/zipkin",
        refreshonly => true,
        subscribe => Exec["git clone zipkin"]
    }
}