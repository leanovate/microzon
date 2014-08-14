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

    file { "/opt/customer/customer.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/dists/customer-0.1.0.jar",
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}