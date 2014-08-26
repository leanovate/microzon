class zipkin_collector::install {
    package { "nginx":
        ensure => present
    }

    package { "supervisor":
        ensure => present
    }

    package { "unzip":
        ensure => present
    }

    file { ["/opt/app", "/opt/app/zipkin-collector-service", "/opt/app/zipkin-query-service", "/opt/app/zipkin-web"]:
        ensure => directory,
        owner => root,
        group => root,
        mode => 755
    }

    exec { "download collector":
        command => "/usr/bin/wget https://dl.dropboxusercontent.com/u/3815280/zipkin-collector-service.zip",
        cwd => "/opt/app",
        creates => "/opt/app/zipkin-collector-service.zip"
    }

    exec { "unpack collector":
        command => "/usr/bin/unzip /opt/app/zipkin-collector-service.zip",
        cwd => "/opt/app/zipkin-collector-service",
        subscribe => Exec["download collector"],
        require => Package["unzip"],
        refreshonly => true
    }

    exec { "download query":
        command => "/usr/bin/wget https://dl.dropboxusercontent.com/u/3815280/zipkin-query-service.zip",
        cwd => "/opt/app",
        creates => "/opt/app/zipkin-query-service.zip"
    }

    exec { "unpack query":
        command => "/usr/bin/unzip /opt/app/zipkin-query-service.zip",
        cwd => "/opt/app/zipkin-query-service",
        subscribe => Exec["download query"],
        require => Package["unzip"],
        refreshonly => true
    }

    exec { "download web":
        command => "/usr/bin/wget https://dl.dropboxusercontent.com/u/3815280/zipkin-web.zip",
        cwd => "/opt/app",
        creates => "/opt/app/zipkin-web.zip"
    }

    exec { "unpack web":
        command => "/usr/bin/unzip /opt/app/zipkin-web.zip",
        cwd => "/opt/app/zipkin-web",
        subscribe => Exec["download web"],
        require => Package["unzip"],
        refreshonly => true
    }
}