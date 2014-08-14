class app_customer::config {
    file { "/var/log/application":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }
    
    file { "/opt/customer/application.properties":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/app_customer/application.properties"
    }

    file { "/etc/supervisor/conf.d/customer.conf":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/app_customer/customer.conf"
    }
}