class app_billing::config {
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

    file { "/opt/billing/billing.yml":
        source => "puppet:///modules/app_billing/billing.yml"
    }

    file { "/etc/supervisor/conf.d/billing.conf":
        source => "puppet:///modules/app_billing/billing.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]        
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/billing":
        source => "puppet:///modules/app_billing/nginx.conf"
    }    

    file { "/etc/consul.d/billing.json":
        source => "puppet:///modules/app_customer/customer.json",
        notify => Service["supervisor"]
    }   

    file { "/etc/consul.d/billing.json":
        source => "puppet:///modules/app_billing/billing.json",
        notify => Service["supervisor"]
    }   
}