class app_customer::install {
    mysql_user { 'customer@localhost':
        ensure        => present,
        password_hash => mysql_password("customer"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'customer':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'customer@localhost/customer.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'customer.*',
        user       => 'customer@localhost',
        require    => [Mysql_user['customer@localhost'], Mysql_database['customer']]
    }

    file { "/opt/customer":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    exec { "download dist":
        command => "/usr/bin/curl -o /tmp/customer.jar ${app_customer::dist_url}",
    }

    file { "/opt/customer/customer.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///tmp/customer.jar",
        require => Exec["download dist"]
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}