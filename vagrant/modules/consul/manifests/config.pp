class consul::config {
    File {
        owner => root,
        group => root,
        mode => 644,
        ensure => file        
    }
    
    file { "/etc/supervisor/conf.d/consul.conf":
        source => "puppet:///modules/consul/consul.conf"
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

    file { "/etc/nginx/sites-enabled/product":
        source => "puppet:///modules/consul/nginx.conf"
    }
}