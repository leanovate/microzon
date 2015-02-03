class app_cart::config {
    file { "/var/log/application":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }
    
    File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file        
    }
    
    file { "/etc/supervisor/conf.d/product.conf":
        source => "puppet:///modules/app_cart/cart.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]        
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/product":
        source => "puppet:///modules/app_cart/nginx.conf"
    }

    file { "/etc/consul.d/cart.json":
        source => "puppet:///modules/app_cart/cart.json",
        notify => Service["supervisor"]
    }   

    file { "/etc/consul.d/alive.py":
        source => "puppet:///modules/app_cart/alive.py",
        mode => 755,
        notify => Service["supervisor"]
    }   
}