class zipkin_collector::config {
    file { "/var/log/zipkin":
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

    file { "/etc/supervisor/conf.d/collector.conf":
        source => "puppet:///modules/zipkin_collector/collector.conf"
    }

    file { "/etc/supervisor/conf.d/query.conf":
        source => "puppet:///modules/zipkin_collector/query.conf"
    }

    file { "/etc/supervisor/conf.d/web.conf":
        source => "puppet:///modules/zipkin_collector/web.conf"
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/web":
        source => "puppet:///modules/zipkin_collector/nginx.conf"
    }

    file { "/etc/consul.d/zipkin.json":
        source => "puppet:///modules/zipkin_collector/zipkin.json",
        notify => Service["supervisor"]
    }
}