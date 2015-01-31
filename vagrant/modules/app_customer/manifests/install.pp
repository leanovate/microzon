class app_customer::install {
    file { "/opt/customer":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download dist":
        command => "/usr/bin/curl -o /tmp/customer.jar ${app_customer::dist_url}",
    }

    file { "/opt/customer/customer.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/customer.jar",
        require => Exec["download dist"]
    }

    package { "nginx":
        ensure => present
    }    
}