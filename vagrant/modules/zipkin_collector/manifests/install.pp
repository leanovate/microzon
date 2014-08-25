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

    file { "/opt/app/zipkin-collector-service.zip":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/vendor/zipkin-collector-service.zip",
    }

    exec { "unpack collector":
        command => "/usr/bin/unzip /opt/app/zipkin-collector-service.zip",
        cwd => "/opt/app/zipkin-collector-service",
        subscribe => File["/opt/app/zipkin-collector-service.zip"],
        require => Package["unzip"],
        refreshonly => true
    }

    file { "/opt/app/zipkin-query-service.zip":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/vendor/zipkin-query-service.zip",
    }

    exec { "unpack query":
        command => "/usr/bin/unzip /opt/app/zipkin-query-service.zip",
        cwd => "/opt/app/zipkin-query-service",
        subscribe => File["/opt/app/zipkin-query-service.zip"],
        require => Package["unzip"],
        refreshonly => true
    }

    file { "/opt/app/zipkin-web.zip":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/vendor/zipkin-web.zip",
    }

    exec { "unpack web":
        command => "/usr/bin/unzip /opt/app/zipkin-web.zip",
        cwd => "/opt/app/zipkin-web",
        subscribe => File["/opt/app/zipkin-web.zip"],
        require => Package["unzip"],
        refreshonly => true
    }
}