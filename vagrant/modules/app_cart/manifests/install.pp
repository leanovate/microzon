class app_cart::install {
    file { "/opt/cart":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download cart dist":
        command => "/usr/bin/curl -o /tmp/cart.jar ${app_cart::dist_url}",
    }

    file { "/opt/cart/cart.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/cart.jar",
        require => Exec["download cart dist"]
    }

    package { "nginx":
        ensure => present
    }    
}