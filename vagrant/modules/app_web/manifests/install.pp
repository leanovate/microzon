class app_web::install {
    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    

    file { "/opt/app":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    file { "/opt/app/web.zip":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/dists/web-0.1.0.zip",
    }

    exec { "unpack web.zip":
        command => "/bin/rm -rf web; /usr/bin/unzip web.zip; /bin/mv web-0.1.0 web",
        cwd => "/opt/app",
        subscribe => File["/opt/app/web.zip"],
        require => Package["unzip"]
    }
}