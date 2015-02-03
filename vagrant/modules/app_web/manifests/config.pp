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
        source => "puppet:///modules/app_web/web.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]        
    }

    file { "/opt/app/web.conf":
        source => "puppet:///modules/app_web/application.conf",
        notify => Service["supervisor"],
        require => Package["supervisor"]        
    }

    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/web":
        source => "puppet:///modules/app_web/nginx.conf"
    }


    file { "/etc/consul.d/web.json":
        source => "puppet:///modules/app_web/web.json",
        notify => Service["supervisor"]
    }   

    file { "/etc/consul.d/alive.py":
        source => "puppet:///modules/app_web/alive.py",
        mode => 755,
        notify => Service["supervisor"]
    }   
}