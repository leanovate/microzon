
class app_mysql::cart {
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
}