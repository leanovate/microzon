class app_cart::install {
    mysql_user { 'cart@localhost':
        ensure        => present,
        password_hash => mysql_password("cart"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'cart':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'customer@localhost/cart.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'cart.*',
        user       => 'cart@localhost',
        require    => [Mysql_user['cart@localhost'], Mysql_database['cart']]
    }    

    file { "/opt/cart":
        owner => root,
        group => root,
        mode => 755,
        ensure => directory
    }

    file { "/opt/cart/cart.jar":
        owner => root,
        group => root,
        mode => 644,
        ensure => file,
        source => "file:///vagrant/dists/cart-assembly-0.1.0.jar",
    }

    package { "supervisor":
        ensure => present
    }

    package { "nginx":
        ensure => present
    }    
}