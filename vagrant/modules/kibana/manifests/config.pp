class kibana::config {
    file { "/etc/nginx/sites-enabled/default":
        ensure => absent
    }

    file { "/etc/nginx/sites-enabled/kibana":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/kibana/nginx.conf"
    }

    file { "/usr/share/kibana3/config.js":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "puppet:///modules/kibana/config.js"
    }
}