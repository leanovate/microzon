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
        source => "puppet:///modules/app_billing/billing.conf"
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/billing":
        source => "puppet:///modules/app_billing/nginx.conf"
    }    
}