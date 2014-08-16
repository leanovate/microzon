class app_web::config {
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

    file { "/etc/supervisor/conf.d/web.conf":
        source => "puppet:///modules/app_web/web.conf"
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/web":
        source => "puppet:///modules/app_web/nginx.conf"
    }
}