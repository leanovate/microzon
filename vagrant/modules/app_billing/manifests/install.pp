class app_billing::install {
    mysql_user { 'billing@localhost':
        ensure        => present,
        password_hash => mysql_password("billing"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'billing':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'billing@localhost/cart.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'billing.*',
        user       => 'billing@localhost',
        require    => [Mysql_user['billing@localhost'], Mysql_database['billing']]
    }    

    file { "/opt/billing":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download billing dist":
        command => "/usr/bin/curl -o /tmp/billing.jar ${app_billing::dist_url}",
    }

    file { "/opt/billing/billing.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/billing.jar",
        require => Exec["download billing dist"]
    }

    package { "nginx":
        ensure => present
    }    
}