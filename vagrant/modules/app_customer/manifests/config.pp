class app_customer::config {
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

    file { "/opt/customer/application.properties":
        source => "puppet:///modules/app_customer/application.properties"
    }

    file { "/etc/supervisor/conf.d/customer.conf":
        source => "puppet:///modules/app_customer/customer.conf"
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/customer":
        source => "puppet:///modules/app_customer/nginx.conf"
    }

    file { "/etc/consul.d/customer.json":
        source => "puppet:///modules/app_customer/customer.json",
        notify => Service["supervisor"]
    }   

    file { "/etc/consul.d/alive.py":
        source => "puppet:///modules/app_customer/alive.py",
        notify => Service["supervisor"]
    }   
}