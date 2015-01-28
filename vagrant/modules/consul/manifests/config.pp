class consul::config {
    file { "/var/log/application":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    file { "/var/log/supervisor":
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
    
    file { "/etc/supervisor/conf.d/consul.conf":
        source => "puppet:///modules/consul/consul.conf",
        notify => Service["supervisor"]
    }

    file { "/etc/consul.d/consul.json":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/consul/consul.json"
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/consul":
        source => "puppet:///modules/consul/nginx.conf"
    }
}