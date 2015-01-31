
class app_mysql::cart {
    mysql_user { 'cart@%':
        ensure        => present,
        password_hash => mysql_password("cart"),
        require => File["/root/.my.cnf"]
    }

    mysql_database { 'cart':
        ensure => present,
        require => File["/root/.my.cnf"]
    }

    mysql_grant { 'cart@%/cart.*':
        ensure     => 'present',
        options    => ['GRANT'],
        privileges => ['ALL'],
        table      => 'cart.*',
        user       => 'cart@%',
        require    => [Mysql_user['cart@%'], Mysql_database['cart']]
    }    	
}