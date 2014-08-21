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

    file { "/opt/app":
        ensure => directory,
        owner => root,
        group => root,
        mode => 755
    }

    exec { "download collector":
        command => "/usr/bin/wget https://github.com/twitter/zipkin/releases/download/1.1.0/zipkin-collector-service.zip",
        cwd => "/tmp",
        creates => "/tmp/zipkin-collector-service.zip"
    }

    exec { "unpack collector":
        command => "/bin/rm -rf zipkin-collector-service; /usr/bin/unzip /tmp/zipkin-collector-service.zip; /bin/mv zipkin-collector-service-1.1.0 zipkin-collector-service",
        cwd => "/opt/app",
        subscribe => Exec["download collector"],
        require => Package["unzip"]
    }

    exec { "download query":
        command => "/usr/bin/wget https://github.com/twitter/zipkin/releases/download/1.1.0/zipkin-query-service.zip",
        cwd => "/tmp",
        creates => "/tmp/zipkin-query-service.zip"
    }

    exec { "unpack query":
        command => "/bin/rm -rf zipkin-query-service; /usr/bin/unzip /tmp/zipkin-query-service.zip; /bin/mv zipkin-query-service-1.1.0 zipkin-query-service",
        cwd => "/opt/app",
        subscribe => Exec["download query"],
        require => Package["unzip"]
    }

    exec { "download web":
        command => "/usr/bin/wget https://github.com/twitter/zipkin/releases/download/1.1.0/zipkin-web.zip",
        cwd => "/tmp",
        creates => "/tmp/zipkin-web.zip"
    }

    exec { "unpack web":
        command => "/bin/rm -rf zipkin-web; /usr/bin/unzip /tmp/zipkin-web.zip; /bin/mv zipkin-web-1.1.0 zipkin-web",
        cwd => "/opt/app",
        subscribe => Exec["download web"],
        require => Package["unzip"]
    }

    exec { "unpack web resources":
        command => "/bin/rm -rf resources; /bin/mkdir resources; cd resources; /usr/bin/jar xf ../zipkin-web-1.1.0.jar",
        cwd => "/opt/app/zipkin-web",
        subscribe => Exec["unpack web"]
    }
}