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

    file { "/opt/billing/billing.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/dists/billing-0.1.0-standalone.jar",
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}